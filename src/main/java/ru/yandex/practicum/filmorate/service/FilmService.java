package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void createLike(int idFilm, int userId) {
        if (idFilm <= 0 & userId <= 0) {
            throw new NotFoundException("id меньше или равно 0");
        }
        Film film = filmStorage.getFilmById(idFilm);
        if (film == null) {
            log.info("Не удалось добавить Like к фильму");
            throw new NotFoundException("Фильм не найден");
        }
        film.getLikes().add(userId);
        try {
            filmStorage.updateFilm(film);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
        log.info("Like успешно создан");
    }

    public void deleteLikeFilm(int idFilm, int userId) {
        Film film = filmStorage.getFilmById(idFilm);
        if (filmStorage.getFilmById(idFilm) == null) {
            log.info("Не удалось удалить Like к фильму");
            throw new NotFoundException("Фильм не найден");
        }
        film.getLikes().remove(userId);
        log.warn("Like успешно удален");
    }

    public List<Film> returnListBestFilms(Integer count) {
        return filmStorage.getPopularFilm(count);
    }
}
