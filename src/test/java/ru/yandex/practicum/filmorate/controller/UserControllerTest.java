package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import javax.validation.ValidationException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    UserController userController = new UserController(new UserService(inMemoryUserStorage), inMemoryUserStorage);

    @Test
    void returnValidateExceptionForBirthdayUser() {

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

    @Test
    void getUserByIdTest() {

        User user = new User();
        user.setId(0);
        user.setEmail("pochta@mail.ru");
        user.setLogin("homosapiens");
        user.setName("Vasya");
        user.setBirthday(LocalDate.of(1999, 01, 01));

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> {
                    userController.getUserById(user.getId());
                });

        assertEquals("ID меньше или равно 0", exception.getMessage());
    }

    @Test
    void updateUserTest() {

        User user = new User();
        user.setId(0);
        user.setEmail("pochta@mail.ru");
        user.setLogin("homosapiens");
        user.setName("Vasya");
        user.setBirthday(LocalDate.of(1999, 01, 01));

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> {
                    userController.updateUser(user);
                });

        assertEquals("ID меньше или равно 0", exception.getMessage());
    }

}
