package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;


@Primary
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    protected final JdbcTemplate jdbc;
    protected final UserDbStorage userDbStorage;
    protected final FilmRowMapper mapper;
    protected final MpaDbStorage mpaStorage;
    protected final GenreDbStorage genreStorage;

    private static final String FIND_ALL_QUERY = "SELECT * FROM FILMS";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM FILMS WHERE FILM_ID = ?";
    private static final String FIND_MPA_QUERY = "SELECT FILM_MPA FROM FILMS WHERE FILM_ID = ?";
    private static final String FIND_LIST_OF_GENRES_QUERY = "SELECT GENRE_ID FROM FILMS_GENRES WHERE FILM_ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE," +
            "DURATION, FILM_MPA) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_FILMS_GENRES_QUERY = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
    private static final String DELETE_FILMS_GENRES_QUERY = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
    private static final String UPDATE_QUERY = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?," +
            "RELEASE_DATE = ?, DURATION = ?, FILM_MPA = ? WHERE FILM_ID = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM FILMS WHERE FILM_ID = ?";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
    private static final String GET_POPULAR_FILMS = "SELECT  FILM_ID  FROM LIKES GROUP BY FILM_ID ORDER BY COUNT(USER_ID) DESC LIMIT(?)";

    @Override
    public Film add(Film film) {
        validate(film);
        Long mpaId = film.getMpa().getId();
        List<Genre> genres = film.getGenres();
        mpaStorage.get(mpaId);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_QUERY, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, film.getReleaseDate());
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        film.setLikes(new HashSet<>());
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                genreStorage.getGenre(genre.getId());
            }
            jdbc.batchUpdate(
                    INSERT_FILMS_GENRES_QUERY,
                    genres,
                    genres.size(),
                    (PreparedStatement ps, Genre genre) -> {
                        ps.setLong(1, film.getId());
                        ps.setLong(2, genre.getId());
                    }
            );
        }
        film.setGenres(genres == null ? new ArrayList<>() : new ArrayList<>(new HashSet<>(genres)));
        film.setMpa(mpaStorage.get(mpaId));

        return film;
    }


    public void validate(Film film) {
        if (film.getReleaseDate().toLocalDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не должна быть раньше 28 декабря 1895 года");
        }
    }

    @Override
    public Film update(Film film) {
        try {
            Film filmToUpdate = get(film.getId());
            List<Genre> genresToUpdate = filmToUpdate.getGenres();

            if (genresToUpdate != null) {
                jdbc.update(DELETE_FILMS_GENRES_QUERY, filmToUpdate.getId());
            }

            validate(film);
            updateFilm(
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId()
            );

            List<Genre> updatedGenres = film.getGenres();
            film.setGenres(new ArrayList<>());

            if (updatedGenres != null && !updatedGenres.isEmpty()) {
                List<Object[]> genreParams = new ArrayList<>();
                for (Genre genre : updatedGenres) {
                    genreParams.add(new Object[]{film.getId(), genre.getId()});
                }
                jdbc.batchUpdate(INSERT_FILMS_GENRES_QUERY, genreParams);

                film.setGenres(updatedGenres);
            }
            return film;
        } catch (NullPointerException e) {
            throw new NotFoundException("Такого фильма нет.");
        }
    }


    protected void updateFilm(Object... params) {
        int rowsUpdated = jdbc.update(UPDATE_QUERY, params);
        if (rowsUpdated == 0) {
            throw new NotFoundException("Не удалось обновить данные");
        }
    }

    @Override
    public Film get(long id) {
        Film filmToReturn;
            filmToReturn = jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
            Long mpaId = jdbc.queryForObject(FIND_MPA_QUERY, Long.class, id);
            if (mpaId != null) {
                filmToReturn.setMpa(mpaStorage.get(mpaId));
            }
            List<Genre> listOfGenres = jdbc.query(FIND_LIST_OF_GENRES_QUERY,
                    (rs, rowNum) -> genreStorage.getGenre(rs.getLong("genre_id")), id);
            List<Genre> uniqueGenres = listOfGenres.stream()
                    .distinct()
                    .toList();

            filmToReturn.setGenres(uniqueGenres);
        return filmToReturn;
    }

    @Override
    public boolean delete(long id) {
        return jdbc.update(DELETE_BY_ID_QUERY, id) > 0;
    }

    @Override
    public Collection<Film> getAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public void addLike(Long id, Long userId) {
        Film currentFilm = get(id);
        User currentUser = userDbStorage.get(userId);
        currentFilm.getLikes().add(userId);

        jdbc.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_LIKE_QUERY, new String[]{});
            stmt.setLong(1, id);
            stmt.setLong(2, userId);
            return stmt;
        });
    }

    @Override
    public boolean deleteLike(Long id, Long userId) {
        Film currentFilm = get(id);
        User currentUser = userDbStorage.get(userId);
        currentFilm.getLikes().remove(userId);

        return jdbc.update(DELETE_LIKE_QUERY, id, userId) > 0;
    }

    @Override
    public List<Film> getPopular(Long count) {
        List<Long> listOfFilms = jdbc.queryForList(GET_POPULAR_FILMS, Long.class, count);
        List<Film> popularFilms = new ArrayList<>();

        for (Long filmId : listOfFilms) {
            popularFilms.add(get(filmId));
        }
        return popularFilms;
    }
}