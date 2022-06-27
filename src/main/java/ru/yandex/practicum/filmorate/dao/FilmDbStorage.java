package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingFilm;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Qualifier("filmDbStorage")
    @Override
    public List<Film> returnAllFilms() {

        String sql = "SELECT f.*, r.name AS rating_name FROM film AS f JOIN rating_film AS r " +
                "ON f.rating_mpa_id = r.rating_film_id ORDER BY f.film_id DESC";

        ResultSetExtractor<List<Film>> extractor = rs -> makeListFilm(rs);
        return jdbcTemplate.query(sql, extractor);
    }

    private List<Film> makeListFilm(ResultSet rs) throws SQLException {
        List<Film> allFilm = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("film_id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
            int duration = rs.getInt("duration");
            int rate = rs.getInt("rate");
            RatingFilm mpa = new RatingFilm();
            mpa.setId(rs.getInt("rating_mpa_id"));
            mpa.setName(rs.getString("rating_name"));
            Film film = new Film(id, name, description, releaseDate, duration, rate, mpa);
            film.setGenres(getListGenreForFilm(id));
            allFilm.add(film);
        }
        return allFilm;
    }

    @Override
    public List<Film> getPopularFilm(int count) {
        String sql = "SELECT f.film_id FROM film f LEFT JOIN likes l ON f.film_id = l.film_id GROUP BY f.film_id " +
                "ORDER BY COUNT (l.film_id) DESC LIMIT ?";
        List<Integer> id = jdbcTemplate.query(sql, ((rs, rowNum) -> rs.getInt("film_id")), count);
        return id.stream().map(this::getFilmById).collect(Collectors.toList());
    }

    @Override
    public Film createFilm(Film film) throws ValidationException {
        if (film.getMpa() == null) {
            throw new ValidationException("MPA не найден");
        }
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("film").usingGeneratedKeyColumns("film_id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", Date.valueOf(film.getReleaseDate()))
                .addValue("duration", film.getDuration())
                .addValue("rate", film.getRate())
                .addValue("rating_mpa_id", film.getMpa().getId());
        Number num = jdbcInsert.executeAndReturnKey(parameters);
        film.setId(num.intValue());
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            film.setGenres(null);
        } else {
            String sql = "INSERT INTO genre_film (film_id, genre_id) VALUES (?,?)";
            film.getGenres().stream().map(Genre::getId).forEach(id -> jdbcTemplate.update(sql, film.getId(), id));
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Set<Genre> genres = film.getGenres();

        String sqlQuery2 = "DELETE FROM genre_film WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery2, film.getId());

        String sqlQuery3 = "DELETE FROM likes WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery3, film.getId());

        String sqlQuery = "UPDATE film SET name = ?, description = ?, " +
                "release_date = ?, duration = ?, rate = ?, rating_mpa_id = ? WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());

        if (film.getLikes() != null) {
            for (Integer like : film.getLikes()) {
                jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)",
                        film.getId(), like);
            }
        }
        if (genres != null) {
            for (Genre genre : genres) {
                if (genre.getId() != 0) {
                    jdbcTemplate.update("INSERT INTO genre_film (film_id, genre_id) VALUES (?, ?)",
                            film.getId(), genre.getId());
                }
            }
            Set<Genre> allGenres = getListGenreForFilm(film.getId());
            film.setGenres(allGenres == null ? new HashSet<>() : allGenres);
        }
        return film;
    }

    @Override
    public Film getFilmById(int id) throws NotFoundException {
        if (id <= 0) {
            throw new NotFoundException("ID меньше или равно нулю");
        }
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT f.*, r.name as rating_name FROM film AS f " +
                "JOIN rating_film AS r ON f.rating_mpa_id = r.rating_film_id WHERE film_id = ?", id);
        if (filmRows.next()) {
            RatingFilm mpa = new RatingFilm();
            mpa.setId(filmRows.getInt("RATING_MPA_ID"));
            mpa.setName(filmRows.getString("RATING_NAME"));

            Film film = new Film(
                    filmRows.getInt("film_id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    LocalDate.parse(Objects.requireNonNull(filmRows.getString("release_date"))),
                    filmRows.getInt("duration"),
                    filmRows.getInt("rate"),
                    mpa
            );
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            film.setGenres(getListGenreForFilm(id));

            String sql = "SELECT user_id FROM likes WHERE film_id =?";
            List<Integer> like = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"), film.getId());
            film.setLikes(new HashSet<>(like));
            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return null;
        }
    }

    protected Set<Genre> getListGenreForFilm(int id) throws NotFoundException {
        if (id <= 0) {
            throw new NotFoundException("ID меньше или равно нулю");
        }
        List<Genre> genres = new ArrayList<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT g.* FROM genre_film gf " +
                "JOIN genre g ON gf.genre_id = g.genre_id WHERE film_id = ? ORDER BY genre_id ASC", id);
        while (genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(genreRows.getInt("genre_id"));
            genre.setName(genreRows.getString("name"));

            genres.add(genre);
        }
        Set<Genre> newGenres = new HashSet<>(genres);
        log.error(newGenres.toString());
        if (newGenres.isEmpty()) {
            return null;
        }
        return newGenres;
    }
}


