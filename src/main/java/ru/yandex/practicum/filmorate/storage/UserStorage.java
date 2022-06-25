package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.List;

public interface UserStorage {

    List<User> findAllUsers();

    User create(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    void validationUser(User user) throws ValidationException;

    User getUserById(int id);
}
