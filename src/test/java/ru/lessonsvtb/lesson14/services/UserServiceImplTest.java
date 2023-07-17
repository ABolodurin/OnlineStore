package ru.lessonsvtb.lesson14.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.lessonsvtb.lesson14.entities.Authority;
import ru.lessonsvtb.lesson14.entities.User;
import ru.lessonsvtb.lesson14.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }


    @Test
    void canFindByName() {
        String expected = "user1";

        userService.findByName(expected);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByUsername(stringArgumentCaptor.capture());

        String actual = stringArgumentCaptor.getValue();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willNotLoadNonExistingUserInSecurityModule() {
        String username = "NonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThatThrownBy(() -> userService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("username not found: " + username);
    }

    @Test
    void willLoadExistingUserInSecurityModule() {
        String usernameExpected = "ExistingUser";
        String passwordExpected = "qwerty";
        List<Authority> authoritiesExpected = new ArrayList<>();
        given(userRepository.findByUsername(usernameExpected))
                .willReturn(new User(usernameExpected, passwordExpected, authoritiesExpected));

        UserDetails actual = userService.loadUserByUsername(usernameExpected);

        verify(userRepository).findByUsername(usernameExpected);

        assertThat(actual.getUsername()).isEqualTo(usernameExpected);
        assertThat(actual.getPassword()).isEqualTo(passwordExpected);
        assertThat(actual.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList()))
                .isEqualTo(authoritiesExpected);
    }

}
