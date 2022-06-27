package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingFilm;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Qualifier("mpaDbStorage")
public class MpaDbStorage implements MpaService {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<RatingFilm> getAllMpa() {
        List<RatingFilm> allMpa = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM rating_film");
        while (mpaRows.next()) {
            RatingFilm mpa = new RatingFilm();
            mpa.setId(mpaRows.getInt("rating_film_id"));
            mpa.setName(mpaRows.getString("name"));
            allMpa.add(mpa);
        }
        return allMpa;
    }

    @Override
    public RatingFilm getMpaById(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM rating_film WHERE rating_film_id = ?", id);
        RatingFilm mpa = new RatingFilm();
        while (mpaRows.next()) {
            mpa.setId(mpaRows.getInt("rating_film_id"));
            mpa.setName(mpaRows.getString("name"));
        }
        return mpa;
    }
}
