package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Slf4j
@EqualsAndHashCode
public class User {

    private int id;
    @Email()
    @NonNull
    private String email;
    @NotBlank
    @Size(min = 1)
    private String login;
    private final String name;
    private final LocalDate birthday;

}
