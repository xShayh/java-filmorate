package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {

    private final FilmController filmController = new FilmController();

    @Test
    void shouldThrowExceptionIfReleaseDateIsTooEarly() {
        Film invalidFilm = new Film();
        invalidFilm.setName("Valid name");
        invalidFilm.setDescription("Valid description");
        invalidFilm.setReleaseDate(LocalDate.of(1895, 12, 27));
        invalidFilm.setDuration(90);

        ValidationException exception = assertThrows(ValidationException.class, () ->
                filmController.addFilm(invalidFilm));
        assert exception.getMessage().equals("Дата релиза не может быть раньше 28 декабря 1895 года");
    }

    @Test
    void shouldPassValidationForValidFilm() {
        Film validFilm = new Film();
        validFilm.setName("Name");
        validFilm.setDescription("Description");
        validFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        validFilm.setDuration(90);

        Film addedFilm = filmController.addFilm(validFilm);

        assert addedFilm != null;
        assert addedFilm.getId() != null;
    }
}