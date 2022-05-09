package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;

@RestController
@Slf4j
public class UserController {
    private HashMap<Integer, User> users = new HashMap();
    int id;

    @GetMapping("/users")
    public HashMap<Integer, User> findAll() {
        log.info("Получен запрос списка пользователей");
        return users;
    }

    @Valid
    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.info("Получен запрос на создание нового пользователя");

            if (user.getBirthday().isBefore(LocalDate.now()) && user.getEmail().contains("@")) {
                createId();
                user.setId(id);
                users.put(user.getId(), user);
            }else {
                System.out.println("Email введен некорректно");
            }
        return users.get(user.getId());
        }

    @Valid
    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        log.info("");
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    private int createId(){
        return id++;
    }

}
