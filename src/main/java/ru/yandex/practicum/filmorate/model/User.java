package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Slf4j
public class User {

    private int id;
    @Email()
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 1)
    private String login;
    private String name;
    private LocalDate birthday;

}
