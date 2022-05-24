package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.List;

public interface FilmStorage {

    List<Film> returnAllFilms();

    Film createFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException;

    Film getFilmById(int filmId);



}
