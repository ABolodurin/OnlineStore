package ru.lessonsvtb.lesson14.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.lessonsvtb.lesson14.entities.User;

public interface UserService extends UserDetailsService {
    User findByName(String username);

}
