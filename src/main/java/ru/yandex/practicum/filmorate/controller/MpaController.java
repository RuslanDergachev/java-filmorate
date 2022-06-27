package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingFilm;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@Component
@RestController
public class MpaController {

    private final MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }


    @GetMapping("/mpa")
    public List<RatingFilm> returnAllMpa() {
        log.info("Запрос списка рейтигов получен.");
        return mpaService.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public RatingFilm getMpa(@PathVariable int id) {
        log.info("Получен запрос на получение рейтинга");
        if (id <= 0) {
            throw new NotFoundException("ID меньше или равно 0");
        }
        return mpaService.getMpaById(id);
    }
}
