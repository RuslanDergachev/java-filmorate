package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int idFilm;
    private final HashMap<Integer, Film> films = new HashMap<>();

    public List<Film> returnAllFilms() {
        return new ArrayList<>(films.values());
    }

    public Film createFilm(Film film) {
        log.warn("Ошибка создания фильма");
        film.setId(++idFilm);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    public Film updateFilm(Film film) {
        log.warn("Ошибка обновления фильма");
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    public Film getFilmById(int filmId) {
        return films.get(filmId);
    }
}
