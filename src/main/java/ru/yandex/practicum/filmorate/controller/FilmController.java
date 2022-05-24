package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RestController
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @GetMapping("/films")
    public List<Film> allFilms() {
        log.info("Запрос списка всех фильмов получен.");
        return filmStorage.returnAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Получен запрос на получение фильма");
        if (id <= 0) {
            throw new NotFoundException("ID равно 0");
        }
        return filmStorage.getFilmById(id);
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Запрос на добавление фильма получен.");
        validationDataReleaseFilm(film);
        return filmStorage.createFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Запрос на обновление фильма");
        if (film.getId() <= 0) {
            throw new NotFoundException("ID меньше или равно 0");
        }
        validationDataReleaseFilm(film);
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void setLikeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос на создание лайка к фильму");
        filmService.createLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос на удаление лайка к фильму");
        if (id <= 0 || userId <= 0) {
            throw new NotFoundException("ID меньше или равно 0");
        }
        filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> returnBestFilms(
            @RequestParam(name = "count", defaultValue = "10", required = false) Integer count
    ) {
        log.info("Получен запрос на получение списка лучших фильмов");

        return filmService.returnListBestFilms(count);
    }

    public void validationDataReleaseFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма ранее 28.12.1895");
        }
    }
}

