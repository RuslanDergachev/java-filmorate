package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int idFilm;

    @GetMapping("/films")
    public List<Film> allFilms() {
        log.info("Запрос списка всех фильмов получен.");
        List<Film> listFilms = new ArrayList<>(films.values());
        return listFilms;
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Запрос на добавление фильма получен.");
        validationDataReleaseFilm(film);
        film.setId(idFilm++);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @PutMapping("/films")
    public void updateFilm(@Valid @RequestBody Film film) {
        log.info("Запрос на обновление фильма");
        validationDataReleaseFilm(film);
        films.put(film.getId(), film);
    }

    public void validationDataReleaseFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма ранее 28.12.1895");
        }
    }
}
