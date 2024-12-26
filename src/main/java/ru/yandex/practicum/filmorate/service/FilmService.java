package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public void add(Film film) {
        filmStorage.add(film);
    }

    public void update(Film newFilm) {
        filmStorage.update(newFilm);
    }

    public Film get(long id) {
        return filmStorage.get(id);
    }

    public void delete(long id) {
        filmStorage.delete(id);
    }

    public void addLike(Long id, Long userId) {
        boolean userExists = userStorage.getAll().stream()
                .anyMatch(user -> user.getId().equals(userId));
        if (userExists) {
            filmStorage.addLike(id, userId);
        } else {
            throw new NotFoundException("Указанного пользователя не существует.");
        }
    }

    public void deleteLike(Long id, Long userId) {
        boolean userExists = userStorage.getAll().stream()
                .anyMatch(user -> user.getId().equals(userId));
        if (userExists) {
            filmStorage.deleteLike(id, userId);
        } else {
            throw new NotFoundException("Указанного пользователя не существует.");
        }
    }

    public List<Film> getPopular(Long count) {
        return filmStorage.getPopular(count);
    }
}