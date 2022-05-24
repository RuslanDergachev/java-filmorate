package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
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
        return user;
    }

    public User updateUser(User user) throws ValidationException {
        validationUser(user);
        users.put(user.getId(), user);
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
        return users.get(id);
    }
}
