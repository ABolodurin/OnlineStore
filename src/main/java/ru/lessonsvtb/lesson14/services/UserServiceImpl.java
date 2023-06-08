package ru.lessonsvtb.lesson14.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.lessonsvtb.lesson14.entities.Authority;
import ru.lessonsvtb.lesson14.entities.User;
import ru.lessonsvtb.lesson14.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User findByName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByName(username);
        if (user == null) throw new UsernameNotFoundException("username not found" + username);
        return new org.springframework.security.core.userdetails
                .User(user.getUsername(), user.getPassword(), mapUserAuthorities(user.getAuthorities()));
    }

    private Collection<? extends GrantedAuthority> mapUserAuthorities(Collection<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());
    }

}
