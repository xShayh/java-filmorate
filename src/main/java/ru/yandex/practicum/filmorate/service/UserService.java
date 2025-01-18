package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserDbStorage userDbStorage;

    public Collection<User> getAll() {
        return userDbStorage.getAll();
    }

    public User add(User user) {
        if (user.getEmail().isEmpty()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        userDbStorage.add(user);
        log.info("Добавлен новый пользователь, id={}", user.getId());

        return user;
    }

    public User update(User newUser) {
        userDbStorage.update(newUser);
        return newUser;
    }

    public User get(long id) {
        return userDbStorage.get(id);
    }

    public void delete(long id) {
        userDbStorage.delete(id);
    }

    public void addFriend(Long id, Long friendId) {
        userDbStorage.addFriend(id, friendId);
    }

    public void deleteFromFriends(Long id, Long friendId) {
        userDbStorage.deleteFromFriends(id, friendId);
    }

    public Collection<User> getAllFriends(Long id) {
        try {
            return userDbStorage.getAllFriends(id);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public Collection<User> getCommonFriends(Long id, Long friendId) {
        return userDbStorage.getCommonFriends(id, friendId);
    }
}