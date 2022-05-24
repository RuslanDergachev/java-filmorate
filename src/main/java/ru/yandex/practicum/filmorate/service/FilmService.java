package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private List<Film> bestFilms;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void createLike(int idFilm, int userId) {
        filmStorage.getFilmById(idFilm).getLikes().add(userId);
    }

    public void deleteLikeFilm(int idFilm, int userId) {
        filmStorage.getFilmById(idFilm).getLikes().remove(userId);
    }

    public List<Film> returnListBestFilms(Integer count) {
        return bestFilms = filmStorage.returnAllFilms().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1) {
        int result = Integer.compare(p0.getLikes().size(), p1.getLikes().size());
        return result * -1;
    }
}
