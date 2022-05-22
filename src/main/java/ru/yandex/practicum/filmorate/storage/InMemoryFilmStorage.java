package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private int idFilm;
    private final HashMap<Integer, Film> films = new HashMap<>();

    public List<Film> returnAllFilms(){
        List<Film> listFilms = new ArrayList<>(films.values());
        return listFilms;
    }


    public Film createFilm(Film film) throws ValidationException{
        validationDataReleaseFilm(film);
        film.setId(++idFilm);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    public Film updateFilm(Film film) throws ValidationException {
        validationDataReleaseFilm(film);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    public void validationDataReleaseFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма ранее 28.12.1895");
        }
    }

    public Film getFilmById(int filmId){
        return films.get(filmId);
    }
}
