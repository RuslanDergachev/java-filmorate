package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Constants.DESCENDING_ORDER;

@Slf4j
@Service
public class UserService {

    private UserStorage userStorage;
    private List<User> allFriends;
    private List<User> mutualFriends;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(int userId, int friendId) {
        if (userStorage.getUserById(userId) != null) {
            userStorage.getUserById(userId).getFriends().add(friendId);
            userStorage.getUserById(friendId).getFriends().add(userId);
            log.info("Для пользователя id: "+userId+" успешно добавлен друг id: "+friendId);
        }
    }

    public void deleteFromFriends(int userId, int friendId) {
        if (userStorage.getUserById(userId) != null) {
            userStorage.getUserById(userId).getFriends().remove(friendId);
            userStorage.getUserById(friendId).getFriends().remove(userId);
            log.info("Для пользователя id: "+userId+" успешно удален друг id: "+friendId);
        }
    }

    public List<User> returnListFriends(int userId, String sort) {
        allFriends = userStorage.getUserById(userId).getFriends().stream()
                .map(userStorage::getUserById)
                .sorted((p0, p1) -> compare(p0, p1, sort))
                .collect(Collectors.toList());
        return allFriends;
    }

    public List<User> returnListMutualFriends(int userId, int otherId, String sort) {
        mutualFriends = userStorage.getUserById(userId).getFriends().stream()
                .map(userStorage::getUserById)
                .filter(p -> p.getFriends().contains(otherId))
                .sorted((p0, p1) -> compare(p0, p1, sort))
                .collect(Collectors.toList());

        return mutualFriends;
    }

    private int compare(User p0, User p1, String sort) {
        int result = Integer.compare(p0.getFriends().size(), (p1.getFriends().size())); //прямой порядок сортировки
        if (sort.equals(DESCENDING_ORDER)) {
            result = -1 * result; //обратный порядок сортировки
        }
        return result;
    }
}
