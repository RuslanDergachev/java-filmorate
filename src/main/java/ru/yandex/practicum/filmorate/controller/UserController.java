package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id;

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("Получен запрос списка пользователей");
        List<User> listUsers = new ArrayList<>(users.values());
        return listUsers;
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание нового пользователя");
        validationUser(user);
        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }


    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновление пользователя");
        validationUser(user);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    public void validationUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Недопустимая дата рождения");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
