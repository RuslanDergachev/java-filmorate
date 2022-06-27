package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingFilm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Qualifier("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private int idFilm;
    private final HashMap<Integer, Film> films = new HashMap<>();

    public List<Film> returnAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> getPopularFilm(int count) {
        return null;
    }

    public Film createFilm(Film film) {
        film.setId(++idFilm);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    public Film updateFilm(Film film) {
        if(!films.containsKey(film.getId())) {
            log.info("Обновить фильм не удалось");
            throw new NotFoundException("Фильм с id: "+ film.getId() +" не найден");
        }
        films.put(film.getId(), film);
            log.info("Обновление фильма прошло успешно");
            return films.get(film.getId());
        }

    public Film getFilmById(int filmId) {
        return films.get(filmId);
    }
}
