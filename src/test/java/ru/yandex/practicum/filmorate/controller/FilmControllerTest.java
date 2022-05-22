package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class FilmControllerTest {

    @Test
    void returnValidateExceptionForDataReleaseFilmTest() {
        FilmController filmController = new FilmController(new FilmService(new FilmStorage() {
            @Override
            public List<Film> returnAllFilms() {
                return null;
            }

            @Override
            public Film createFilm(Film film) {
                return null;
            }

            @Override
            public Film updateFilm(Film film) {
            return null;
            }

            @Override
            public void validationDataReleaseFilm(Film film) {

            }

            @Override
            public Film getFilmById(int filmId) {
                return null;
            }
        }), new InMemoryFilmStorage());

        Film newFilm = new Film();
        newFilm.setId(1);
        newFilm.setName("Venom-3");
        newFilm.setDescription("фантастика");
        newFilm.setReleaseDate(LocalDate.of(1894, 01, 01));
        newFilm.setDuration(90);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    filmController.validationDataReleaseFilm(newFilm);
                });

        assertEquals("Дата релиза фильма ранее 28.12.1895", exception.getMessage());
    }
}