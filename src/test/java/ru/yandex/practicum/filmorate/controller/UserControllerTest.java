package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void returnValidateExceptionForBirthdayUser() {
        UserController userController = new UserController(new UserService(new UserStorage() {
            @Override
            public List<User> findAllUsers() {
                return null;
            }

            @Override
            public User create(User user) {
                return null;
            }

            @Override
            public User updateUser(User user) {
                return null;
            }

            @Override
            public void validationUser(User user) {

            }

            @Override
            public User getUserById(int id) {
                return null;
            }
        }), new InMemoryUserStorage());

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
