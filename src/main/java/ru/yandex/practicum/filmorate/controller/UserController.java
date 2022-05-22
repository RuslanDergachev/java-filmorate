package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;

import java.util.List;

import static ru.yandex.practicum.filmorate.Constants.DESCENDING_ORDER;

@RestController
@Component
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        log.info("Получен запрос списка пользователей");
        return userStorage.findAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Получен запрос на получение данных пользователя пользователей");
        if (id <= 0) {
            throw new NotFoundException("ID равно 0");
        }
        return userStorage.getUserById(id);
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        log.info("Получен запрос на создание нового пользователя");
        return userStorage.create(user);
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("Получен запрос на обновление пользователя");
        if (user.getId() <= 0) {
            throw new NotFoundException("ID равно 0");
        }
        return userStorage.updateUser(user);
    }

    public void validationUser(User user) throws ValidationException {
        userStorage.validationUser(user);
    }

    @PutMapping("users/{id}/friends/{friendId}")//добавление в друзья
    public void addToFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос на добавление в друзья");
        if (id <= 0 || friendId <= 0) {
            throw new NotFoundException("ID равно 0");
        }
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("users/{id}/friends/{friendId}")//удаление из друзей
    public void deleteFromFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос на удаление из друзей");
        if (id <= 0 || friendId <= 0) {
            throw new NotFoundException("ID равно 0");
        }
        userService.deleteFromFriends(id, friendId);
    }


    @GetMapping("users/{id}/friends")//возвращаем список друзей
    public List<User> returnListFriends(@PathVariable int id) {
        log.info("Получен запрос на получение списка друзей");
        if (id <= 0) {
            throw new NotFoundException("ID меньше или равно 0");
        }
        if (userService.returnListFriends(id, DESCENDING_ORDER).isEmpty()) {
            throw new NotFoundException("Друзья не найдены");
        }
        return userService.returnListFriends(id, DESCENDING_ORDER);
    }

    @GetMapping("users/{id}/friends/common/{otherId}")//возвращаем список друзей, общих с другим пользователем
    public List<User> returnListMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен запрос на списка общих друзей");
        if (id <= 0 || otherId <= 0) {
            throw new NotFoundException("ID равно 0");
        }
        return userService.returnListMutualFriends(id, otherId, DESCENDING_ORDER);

    }
}
