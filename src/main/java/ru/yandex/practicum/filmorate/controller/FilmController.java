package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) {
        return filmService.get(id);
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        filmService.add(film);
        log.info("Фильм с ID {} добавлен.", film.getId());
        return film;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        filmService.delete(id);
        log.info("Фильм с ID {} удалён.", id);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film updatedFilm) {
        filmService.update(updatedFilm);
        log.info("Фильм с ID {} обновлён.", updatedFilm.getId());
        return updatedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
        log.info("Пользователь с ID {} поставил лайк фильму с ID {}.", userId, id);
        return filmService.get(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
        log.info("Пользователь с ID {} убрал лайк фильму с ID {}.", userId, id);
        return filmService.get(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Long count) {
        if (count <= 0) {
            throw new IncorrectArgumentException("Значение count должно быть больше нуля.");
        }
        return filmService.getPopular(count);
    }

}
