package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {

    InMemoryFilmStorage filmStorage;
    InMemoryUserStorage userStorage;

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
        if (userStorage.getUsers().containsKey(userId)) {
            filmStorage.addLike(id, userId);
        } else {
            throw new NotFoundException("Указанного пользователя не существует.");
        }
    }

    public void deleteLike(Long id, Long userId) {
        if (userStorage.getUsers().containsKey(userId)) {
            filmStorage.deleteLike(id, userId);
        } else {
            throw new NotFoundException("Указанного пользователя не существует.");
        }
    }

    public List<Film> getPopular(Long count) {
        return filmStorage.getPopular(count);
    }

}