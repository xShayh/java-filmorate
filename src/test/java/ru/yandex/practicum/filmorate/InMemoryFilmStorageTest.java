package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryFilmStorageTest {

    private InMemoryFilmStorage filmStorage;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
    }

    @Test
    void shouldAddAndRetrieveFilm() {
        Film film = new Film();
        film.setName("Тестовый фильм");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));

        filmStorage.add(film);
        Film retrievedFilm = filmStorage.get(film.getId());

        assertThat(retrievedFilm).isNotNull();
        assertThat(retrievedFilm.getName()).isEqualTo("Тестовый фильм");
    }

    @Test
    void shouldThrowExceptionForInvalidReleaseDate() {
        Film film = new Film();
        film.setName("Некорректный фильм");
        film.setReleaseDate(LocalDate.of(1800, 1, 1));

        assertThrows(ValidationException.class, () -> filmStorage.add(film));
    }

    @Test
    void shouldUpdateFilm() {
        Film film = new Film();
        film.setName("Original Film");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));

        filmStorage.add(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(film.getId());
        updatedFilm.setName("Обновлённый фильм");
        updatedFilm.setReleaseDate(LocalDate.of(2001, 1, 1));

        filmStorage.update(updatedFilm);
        Film retrievedFilm = filmStorage.get(film.getId());

        assertThat(retrievedFilm.getName()).isEqualTo("Обновлённый фильм");
        assertThat(retrievedFilm.getReleaseDate()).isEqualTo(LocalDate.of(2001, 1, 1));
    }
}
