package ru.lessonsvtb.lesson14.entities;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_authorities",
    joinColumns = @JoinColumn(name = "username"),
    inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<Authority> authorities;

}
