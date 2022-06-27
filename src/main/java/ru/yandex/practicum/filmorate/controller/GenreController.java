package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@Component
@RestController
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public List<Genre> getListGenres() {
        log.info("Запрос списка рейтигов получен.");
        return genreService.getAllGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable int id) {
        log.info("Получен запрос на получение жанра");
        if (id <= 0) {
            throw new NotFoundException("ID меньше или равно 0");
        }
        return genreService.getGenreById(id);
    }
}
