package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private int id;
    private String name;
    @NotBlank
    @Size(min = 1)
    private String login;
    @Email()
    @NotBlank
    private String email;
    private LocalDate birthday;
    Set<Integer> friends = new HashSet<>();

    public User(int id, String name, String login, String email, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
