package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.HashMap;

@RestController
@Slf4j
public class UserController {
    private final HashMap<String, User> users = new HashMap<>();
    int id;

    @GetMapping("/users")
    public HashMap<String, User> findAll() {
        log.info("Получен запрос списка пользователей");
        return users;
    }


    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание нового пользователя");
            if (user.getBirthday().isBefore(LocalDate.now())) {
                user.setId(id++);
                users.put(user.getLogin(), user);
                return user;
            } throw new ValidationException("Введите корректные данные пользователя");
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновление пользователя");
        users.put(user.getLogin(), user);
        return users.get(user.getLogin());
    }
}
