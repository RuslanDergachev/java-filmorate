package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.NotFoundException;
import ru.yandex.practicum.filmorate.model.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Component
@RestController
public class FilmController {
    private final FilmService filmService;
    private final FilmStorage filmStorage;


    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

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
        return filmStorage.createFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Запрос на обновление фильма");
        if (film.getId() <= 0) {
            throw new NotFoundException("ID меньше или равно 0");
        }
        return filmStorage.updateFilm(film);
    }

    public void validationDataReleaseFilm(Film film) throws ValidationException {
        filmStorage.validationDataReleaseFilm(film);
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
}

