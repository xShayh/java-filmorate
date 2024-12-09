package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей");
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Добавление пользователя: {}", user);
        validateUser(user);
        user.setId(getNextId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь добавлен с ID: {}", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) {
        log.info("Обновление пользователя с ID {}", updatedUser.getId());
        if (!users.containsKey(updatedUser.getId())) {
            log.error("Ошибка обновления: пользователь с ID {} не найден", updatedUser.getId());
            throw new NotFoundException("Пользователь с ID " + updatedUser.getId() + " не найден");
        }
        validateUser(updatedUser);
        if (updatedUser.getName() == null) {
            updatedUser.setName(updatedUser.getLogin());
        }
        users.put(updatedUser.getId(), updatedUser);
        log.info("Пользователь с ID {} обновлён", updatedUser.getId());
        return updatedUser;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Ошибка валидации: email пустой или не содержит символ '@', указанный email: {}", user.getEmail());
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Ошибка валидации: логин пустой или содержит пробелы, указанный логин: {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка валидации: дата рождения в будущем, указанная дата рождения: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        log.info("Пользователь с ID {} успешно прошёл валидацию", user.getId());
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}