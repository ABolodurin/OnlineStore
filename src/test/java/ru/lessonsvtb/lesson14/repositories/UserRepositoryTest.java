package ru.lessonsvtb.lesson14.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.lessonsvtb.lesson14.entities.User;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void reset(){
        userRepository.deleteAll();
    }

    @Test
    void itShouldFindUserByUsername() {
        String username = "username";
        User expected = new User(
                "username",
                "password",
                new ArrayList<>()
        );
        userRepository.save(expected);

        User actual = userRepository.findByUsername(username);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);

    }

    @Test
    void itShouldNotFindUserByUsernameThatNotExists() {
        String username = "username";

        User actual = userRepository.findByUsername(username);

        assertThat(actual).isNull();

    }

}
