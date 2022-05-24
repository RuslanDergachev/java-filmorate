package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int idFilm;
    private final HashMap<Integer, Film> films = new HashMap<>();

    public List<Film> returnAllFilms() {
        return new ArrayList<>(films.values());
    }

    public Film createFilm(Film film) {
        film.setId(++idFilm);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    public Film getFilmById(int filmId) {
        return films.get(filmId);
    }
}
