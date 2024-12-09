package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Запрос на получение всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Добавление фильма: {}", film);
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен с ID: {}", film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) {
        log.info("Обновление фильма с ID {}", updatedFilm.getId());
        if (!films.containsKey(updatedFilm.getId())) {
            log.error("Ошибка обновления фильма: фильм с ID {} не найден", updatedFilm.getId());
            throw new NotFoundException("Фильм с ID " + updatedFilm.getId() + " не найден");
        }
        validateFilm(updatedFilm);
        films.put(updatedFilm.getId(), updatedFilm);
        log.info("Фильм с ID {} обновлён", updatedFilm.getId());
        return updatedFilm;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Ошибка валидации: название фильма пустое");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Ошибка валидации: описание фильма превышает 200 символов, указанная длина: {}",
                    film.getDescription().length());
            throw new ValidationException("Описание фильма не может превышать 200 символов");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка валидации: дата релиза раньше 28 декабря 1895 года, указанная дата: {}",
                    film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() == 0 || !(film.getDuration() > 0)) {
            log.error("Ошибка валидации: отрицательная или нулевая продолжительность фильма, " +
                            "указанная продолжительность: {}",
                    film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        log.info("Фильм с ID {} успешно прошёл валидацию", film.getId());
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
