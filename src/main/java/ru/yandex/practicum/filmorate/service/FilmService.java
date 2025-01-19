package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {
    private FilmDbStorage filmDbStorage;

    public Collection<Film> getAll() {
        return filmDbStorage.getAll();
    }

    public Film add(Film film) {
        try {
            return filmDbStorage.add(film);
        } catch (NotFoundException e) {
            throw new IncorrectArgumentException("Введен некорректный запрос");
        }
    }

    public Film update(Film newFilm) {
        return filmDbStorage.update(newFilm);
    }

    public Film get(long id) {
        Film film = filmDbStorage.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм с ID " + id + " не найден.");
        }
        return film;
    }

    public void delete(long id) {
        filmDbStorage.delete(id);
    }

    public void addLike(Long id, Long userId) {
        try {
            filmDbStorage.addLike(id, userId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Такого пользователя нет.");
        }
    }

    public void deleteLike(Long id, Long userId) {
        try {
            filmDbStorage.deleteLike(id, userId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Такого пользователя нет.");
        }
    }

    public List<Film> getPopular(Long count) {
        return filmDbStorage.getPopular(count);
    }
}