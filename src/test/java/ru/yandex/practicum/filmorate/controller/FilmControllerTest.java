package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class FilmControllerTest {
    InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    FilmController filmController = new FilmController(new FilmService(filmStorage), filmStorage);

    @Test
    void returnValidateExceptionForDataReleaseFilmTest() {

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

    @Test
    void getFilmByIdTest() {

        Film newFilm = new Film();
        newFilm.setId(1);
        newFilm.setName("Venom-3");
        newFilm.setDescription("фантастика");
        newFilm.setReleaseDate(LocalDate.of(1894, 01, 01));
        newFilm.setDuration(90);

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> {
                    filmController.getFilmById(0);
                });

        assertEquals("ID меньше или равно 0", exception.getMessage());

    }

    @Test
    void createFilmTest() throws ValidationException {

        Film newFilm = new Film();
        newFilm.setId(1);
        newFilm.setName("mrak");
        newFilm.setDescription("фантастика");
        newFilm.setReleaseDate(LocalDate.of(1999, 01, 01));
        newFilm.setDuration(90);
        filmController.create(newFilm);


        assertEquals(newFilm, filmController.getFilmById(1));
        assertEquals(1, filmController.getFilmById(1).getId());

    }

    @Test
    void updateFilmByIdTest() throws ValidationException {

        Film newFilm = new Film();
        newFilm.setId(1);
        newFilm.setName("Venom-3");
        newFilm.setDescription("фантастика");
        newFilm.setReleaseDate(LocalDate.of(1999, 01, 01));
        newFilm.setDuration(90);

        Film newFilm2 = new Film();
        newFilm2.setId(0);
        newFilm2.setName("Venom-1");
        newFilm2.setDescription("фантастика");
        newFilm2.setReleaseDate(LocalDate.of(1999, 01, 01));
        newFilm2.setDuration(120);

        filmController.create(newFilm);
        assertEquals(newFilm, filmController.getFilmById(1));

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> {
                    filmController.updateFilm(newFilm2);
                });

        assertEquals("ID меньше или равно 0", exception.getMessage());
    }

}