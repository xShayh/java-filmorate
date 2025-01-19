package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class FilmStorageTest {

    @Autowired
    private FilmStorage filmStorage;

    private Film film;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setName("Test Film");
        film.setDescription("This is a test film.");
        film.setReleaseDate(Date.valueOf("2000-01-01"));
        film.setDuration(120); // 120 минут
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        film.setMpa(mpa);
    }

    @Test
    void addFilmTest() {
        Film addedFilm = filmStorage.add(film);
        assertThat(addedFilm).isNotNull();
        assertThat(addedFilm.getId()).isGreaterThan(0);
    }

    @Test
    void updateFilmTest() {
        Film addedFilm = filmStorage.add(film);
        addedFilm.setName("Updated Film");
        Film updatedFilm = filmStorage.update(addedFilm);

        assertThat(updatedFilm).isNotNull();
        assertThat(updatedFilm.getName()).isEqualTo("Updated Film");
    }

    @Test
    void getFilmTest() {
        Film addedFilm = filmStorage.add(film);
        Film retrievedFilm = filmStorage.get(addedFilm.getId());

        assertThat(retrievedFilm).isNotNull();
        assertThat(retrievedFilm.getName()).isEqualTo(film.getName());
    }

    @Test
    void getAllFilmsTest() {
        filmStorage.add(film);

        Film anotherFilm = new Film();
        anotherFilm.setName("Another Test Film");
        anotherFilm.setDescription("This is another test film.");
        anotherFilm.setReleaseDate(Date.valueOf("1995-05-15"));
        anotherFilm.setDuration(90);
        Mpa mpa = new Mpa();
        mpa.setId(2L);
        anotherFilm.setMpa(mpa);

        filmStorage.add(anotherFilm);

        Collection<Film> films = filmStorage.getAll();
        assertThat(films).hasSize(2);
    }
}
