package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable long id) {
        return userService.get(id);
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        userService.add(user);
        log.info("Пользователь с ID {} добавлен.", user.getId());
        return user;
    }

    @DeleteMapping
    public void deleteById(@PathVariable long id) {
        userService.delete(id);
        log.info("Пользователь с ID {} удалён.", id);
    }

    @PutMapping
    public User update(@Valid @RequestBody User updatedUser) {
        userService.update(updatedUser);
        log.info("Пользователь с ID {} обновлён.", updatedUser.getId());
        return updatedUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
        log.info("Пользователь с ID {} добавил в друзья пользователя с ID {}.", id, friendId);
        return userService.get(friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFromFriends(id, friendId);
        log.info("Пользователь с ID {} удалил из друзей пользователя с ID {}.", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getAllFriends(@PathVariable long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

}