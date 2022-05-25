package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private List<Film> bestFilms;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void createLike(int idFilm, int userId) {
        if (filmStorage.getFilmById(idFilm) == null) {
            log.info("Не удалось добавить Like к фильму");
            throw new NotFoundException("Фильм не найден");
        }
        filmStorage.getFilmById(idFilm).getLikes().add(userId);
        log.info("Like успешно создан");
    }

    public void deleteLikeFilm(int idFilm, int userId) {
        if (filmStorage.getFilmById(idFilm) == null) {
            log.info("Не удалось удалить Like к фильму");
            throw new NotFoundException("Фильм не найден");
        }
        filmStorage.getFilmById(idFilm).getLikes().remove(userId);
        log.warn("Like успешно удален");
    }

    public List<Film> returnListBestFilms(Integer count) {
        return bestFilms = filmStorage.returnAllFilms().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1) {
        int result = Integer.compare(p0.getLikes().size(), p1.getLikes().size());
        return result * -1;
    }
}
