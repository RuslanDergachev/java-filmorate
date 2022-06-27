package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.RatingFilm;

import java.util.List;

public interface MpaService {

    List<RatingFilm> getAllMpa();

    RatingFilm getMpaById(int id);
}
