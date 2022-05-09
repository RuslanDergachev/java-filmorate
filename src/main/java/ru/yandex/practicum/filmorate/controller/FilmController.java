package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;

@RestController
@Slf4j
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    int idFilm;

    @GetMapping("/films")
    public HashMap<Integer, Film> allFilms(){
        log.info("Запрос списка всех фильмов получен.");
        return films;
    }


    @PostMapping("/films")
    public void create(@Valid @RequestBody Film film) {
        log.info("Запрос на добавление фильма получен.");
        if (film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))) {
            createIdFilms();
            film.setId(idFilm);
            films.put(film.getId(), film);
        }
    }

    @PutMapping("/films")
    public void updateFilm(@Valid @RequestBody Film film) {
        log.info("Запрос на обновление фильма");
            films.put(film.getId(), film);
    }

    public int createIdFilms(){
        return idFilm++;
    }
}
