package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final Logger userRows = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAllUsers() {
        // метод принимает в виде аргумента строку запроса, преобразователь и аргумент — id пользователя
        String sql = "select * from users";

        ResultSetExtractor<List<User>> extractor = rs -> makeListUser(rs);
        return jdbcTemplate.query(sql, extractor);
    }

    private List<User> makeListUser(ResultSet rs) throws SQLException {
        List<User> allUser = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("user_id");
            String name = rs.getString("name");
            String login = rs.getString("login");
            String email = rs.getString("e_mail");
            // Получаем дату и конвертируем её из sql.Date в time.LocalDate
            LocalDate birthday = rs.getDate("birthday").toLocalDate();
            allUser.add(new User(id, name, login, email, birthday));
        }
        if (allUser.isEmpty()) {
            return null;
        }
        return allUser;
    }

    @Override
    public User create(User user) throws ValidationException {
        validationUser(user);
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("users").usingGeneratedKeyColumns("user_id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("login", user.getLogin())
                .addValue("e_mail", user.getEmail())
                .addValue("birthday", Date.valueOf(user.getBirthday()));
        Number num = jdbcInsert.executeAndReturnKey(parameters);
        if (user.getFriends().isEmpty()) {
            user.setFriends(null);
        }
        user.setId(num.intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user != null) {
            jdbcTemplate.update("UPDATE USERS SET NAME = ?, LOGIN = ?, E_MAIL = ?, " +
                            "BIRTHDAY = ? where USER_ID = ?", user.getName(),
                    user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());

            jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_ID = ?",
                    user.getId());

            if (user.getFriends() != null) {
                for (Integer friend : user.getFriends()) {
                    jdbcTemplate.update("INSERT INTO FRIENDS (USER_ID, FRIEND_ID, STATUS) values (?, ?, ?)",
                            user.getId(), friend, false);
                }
            }
        }
        log.error("update" + user);
        return user;
    }

    @Override
    public void validationUser(User user) throws ValidationException {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Недопустимая дата рождения");
        }
        if (user.getName().isEmpty() || user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public User getUserById(int userId) {
        List<Integer> allFriends = new ArrayList<>();
        if (userId <= 0) {
            throw new NotFoundException("id меньше или равно 0");
        }
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where user_id = ?", userId);
        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getString("e_mail"),
                    LocalDate.parse(Objects.requireNonNull(userRows.getString("birthday")))
            );
            SqlRowSet userRows2 = jdbcTemplate.queryForRowSet("SELECT * FROM FRIENDS where USER_ID = ?", userId);
            while (userRows2.next()) {
                Integer idFriend = userRows2.getInt("FRIEND_ID");
                allFriends.add(idFriend);
            }
            Set<Integer> friends = new HashSet<>(allFriends);
            user.setFriends(friends);
            log.error(user.toString());
            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            return null;
        }
    }
}
