package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id;

    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) throws ValidationException {
        validationUser(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Пользователь создан успешно");
        return user;
    }

    public User updateUser(User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            log.info("Не удалось обновить данные пользователя");
            throw new NotFoundException("Ошибка обновления данных пользователя");
        }
        validationUser(user);
        users.put(user.getId(), user);
        log.info("Данные пользователя успешно обновлены");
        return users.get(user.getId());
    }

    public void validationUser(User user) throws ValidationException {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Недопустимая дата рождения");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    public User getUserById(int id) {
        if (!users.containsKey(id)){
            log.info("Пользователь "+id+" не найден");
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Пользователь id: "+id+" найден");
        return users.get(id);
    }
}
