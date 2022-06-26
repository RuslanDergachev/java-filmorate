package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class UserDbStorageTest {

    @Autowired
    @Qualifier("userDbStorage")
    UserStorage userStorage;

    @Test
    void findAllUsers() throws ValidationException {
        User user = new User(0,"Vasya", "login", "mail@yandex.ru", LocalDate.of(1979, 6, 1));
        User user2 = new User(0,"Dima", "login", "mail2@yandex.ru", LocalDate.of(1985, 9, 2));
        userStorage.create(user);
        userStorage.create(user2);
        assertNotNull(userStorage.findAllUsers());
    }

    @Test
    void create() throws ValidationException {
        User user = new User(0,"Vasya", "login", "mail@yandex.ru", LocalDate.of(1979, 6, 1));
        User resultUser = userStorage.create(user);
        assertFalse(resultUser.getId()==0);
    }

    @Test
    void updateUser() throws ValidationException {
        User user = new User(0,"Vasya", "login", "mail@yandex.ru", LocalDate.of(1979, 6, 1));
        User user2 = new User(1,"Dima", "login", "mail2@yandex.ru", LocalDate.of(1985, 9, 2));
        userStorage.create(user);
        userStorage.updateUser(user2);
        assertEquals("Dima", userStorage.getUserById(1).getName());

    }

    @Test
    void getUserById() throws ValidationException {
        User user = new User(0,"Vasya", "login", "mail@yandex.ru", LocalDate.of(1979, 6, 1));
        userStorage.create(user);
        assertEquals(1, userStorage.getUserById(1).getId());
    }
}