package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingFilm;

import java.util.List;
import java.util.Set;

public interface FilmStorage {


    List<Film> returnAllFilms();

    Film createFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException;

    Film getFilmById(int filmId);

    List<RatingFilm> getAllMpa();

    RatingFilm getMpaById(int id);

    List<Genre> getAllGenre();

    Genre getGenreById(int id);

    List<Film> getPopularFilm(int count);
}
