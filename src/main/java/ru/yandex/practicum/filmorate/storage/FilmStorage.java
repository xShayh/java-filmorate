package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    void add(Film film);

    void update(Film film);

    Film get(long id);

    void delete(long id);

    Collection<Film> getAll();

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    List<Film> getPopular(Long count);
}
