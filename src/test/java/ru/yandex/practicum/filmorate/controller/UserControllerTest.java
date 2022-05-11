package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void returnValidateExceptionForBirthdayUser() {
        UserController userController = new UserController();

        User user = new User();
        user.setId(1);
        user.setEmail("pochta@mail.ru");
        user.setLogin("homosapiens");
        user.setName("Vasya");
        user.setBirthday(LocalDate.of(2023, 01, 01));

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    userController.validationUser(user);
                });

        assertEquals("Недопустимая дата рождения", exception.getMessage());
    }
}
