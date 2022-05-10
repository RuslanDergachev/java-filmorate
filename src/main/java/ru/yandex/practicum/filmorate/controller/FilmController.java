package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.HashMap;

@RestController
@Slf4j
public class FilmController {

    private final HashMap<String, Film> films = new HashMap<>();
    int idFilm;

    @GetMapping("/films")
    public HashMap<String, Film> allFilms() {
        log.info("Запрос списка всех фильмов получен.");
        return films;
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Запрос на добавление фильма получен.");
        if (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
            film.setId(idFilm++);
            films.put(film.getName(), film);
            return films.get(film.getName());
        }
        throw new ValidationException("Введите корректные данные фильма");
    }

    @PutMapping("/films")
    public void updateFilm(@Valid @RequestBody Film film) {
        log.info("Запрос на обновление фильма");
        films.put(film.getName(), film);
    }
}
