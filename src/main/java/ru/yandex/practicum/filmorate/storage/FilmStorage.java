package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.validation.Valid;
import java.util.List;

public interface FilmStorage {

    List<Film> returnAllFilms();

    Film createFilm(Film film) throws ValidationException;
    Film updateFilm(Film film) throws ValidationException;

    void validationDataReleaseFilm(Film film) throws ValidationException;

    Film getFilmById(int filmId);



}
