package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.RatingFilm;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Constants.DESCENDING_ORDER;

@Slf4j
@Service
public class UserService {


    private UserStorage userStorage;
    private List<User> allFriends;
    private List<User> mutualFriends;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(int userId, int friendId) throws ValidationException {
        if (userId <= 0 & friendId <= 0) {
            throw new NotFoundException("id меньше или равно 0");
        }
        User user = userStorage.getUserById(userId);
        User userFriend = userStorage.getUserById(friendId);
        if (user != null & userFriend != null) {
            user.getFriends().add(friendId);
            //userFriend.getFriends().add(userId);
            userStorage.updateUser(user);
            //userStorage.updateUser(userFriend);
            log.info("Для пользователя id: " + userId + " успешно добавлен друг id: " + friendId);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public void deleteFromFriends(int userId, int friendId) throws ValidationException {
        User user = userStorage.getUserById(userId);
        if (userStorage.getUserById(userId) != null) {
            user.getFriends().remove(friendId);
            userStorage.updateUser(user);
            log.info("Для пользователя id: " + userId + " успешно удален друг id: " + friendId);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public List<User> returnListFriends(int userId, String sort) {
        allFriends = userStorage.getUserById(userId).getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        if (allFriends.isEmpty()) {
            return null;
        }
        return allFriends;
    }

    public List<User> returnListMutualFriends(int userId, int otherId) {
        Set<Integer> userFriends = userStorage.getUserById(userId).getFriends();
        Set<Integer> friendFriends = userStorage.getUserById(otherId).getFriends();
        userFriends.retainAll(friendFriends);
        return userFriends.stream().map(userStorage :: getUserById).collect(Collectors.toList());

    }
}
