package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingFilm;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureTestDatabase
class FilmDbStorageTest {
    @Autowired
    @Qualifier("filmDbStorage")
    FilmStorage filmStorage;
    
    @Test
    void returnAllFilms() throws ValidationException {
        RatingFilm  mpa = new RatingFilm();
        mpa.setId(1);
        Film film = new Film(0, "newFilm", "Normal film",
                LocalDate.of(2000, 10, 01), 120, 1, mpa);
        Film film2 = new Film(0, "Film", "Interesting film",
                LocalDate.of(1999, 10, 01), 120, 3, mpa);

        filmStorage.createFilm(film);
        filmStorage.createFilm(film2);
        assertNotNull(filmStorage.returnAllFilms());
    }

    @Test
    void getPopularFilm() throws ValidationException {
        RatingFilm  mpa = new RatingFilm();
        mpa.setId(1);
        Film film = new Film(0, "newFilm", "Normal film",
                LocalDate.of(2000, 10, 01), 120, 1, mpa);
        Film film2 = new Film(0, "Film", "Interesting film",
                LocalDate.of(1999, 10, 01), 120, 3, mpa);
        filmStorage.createFilm(film);
        filmStorage.createFilm(film2);
        Set<Integer> likes = new HashSet<>();
        likes.add(1);
        likes.add(2);
        film.setLikes(likes);
        Set<Integer> likes2 = new HashSet<>();
        likes.add(1);
        film2.setLikes(likes2);
        assertEquals(2, filmStorage.getPopularFilm(2).size());
    }

    @Test
    void createFilm() throws ValidationException {
        RatingFilm  mpa = new RatingFilm();
        mpa.setId(1);
        Film film = new Film(0, "newFilm", "Normal film",
                LocalDate.of(2000, 10, 01), 120, 1, mpa);
        Film resultFilm = filmStorage.createFilm(film);
        assertFalse(resultFilm.getId() == 0);


    }

    @Test
    void updateFilm() throws ValidationException {
        RatingFilm  mpa = new RatingFilm();
        mpa.setId(1);
        Film film = new Film(0, "newFilm", "Normal film",
                LocalDate.of(2000, 10, 01), 120, 1, mpa);
        Film film2 = new Film(1, "Film", "Interesting film",
                LocalDate.of(1999, 10, 01), 120, 3, mpa);
        filmStorage.createFilm(film);
        filmStorage.updateFilm(film2);
        assertEquals("Film", filmStorage.getFilmById(1).getName());
    }

    @Test
    void getFilmById() throws ValidationException {

        RatingFilm  mpa = new RatingFilm();
        mpa.setId(1);
        Film film = new Film(0, "newFilm", "Normal film",
                LocalDate.of(2000, 10, 01), 120, 1, mpa);
        Film film2 = new Film(0, "Film", "Interesting film",
                LocalDate.of(1999, 10, 01), 120, 3, mpa);
        filmStorage.createFilm(film);
        filmStorage.createFilm(film2);
        assertEquals(2, filmStorage.getFilmById(2).getId());
    }
}