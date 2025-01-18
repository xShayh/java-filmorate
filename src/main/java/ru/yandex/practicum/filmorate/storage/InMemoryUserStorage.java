/*
package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Data
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public void add(@Valid @RequestBody User user) {
        validate(user);
        user.setId(getNextId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public void update(@Valid @RequestBody User updatedUser) {
        if (!users.containsKey(updatedUser.getId())) {
            throw new NotFoundException("Пользователь с ID " + updatedUser.getId() + " не найден");
        }
        validate(updatedUser);
        if (updatedUser.getName() == null) {
            updatedUser.setName(updatedUser.getLogin());
        }
        users.put(updatedUser.getId(), updatedUser);
    }

    @Override
    public User get(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Указанного пользователя не существует.");
        } else {
            return users.get(id);
        }
    }

    @Override
    public void delete(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Указанного пользователя не существует.");
        }

        if (!users.get(id).getFriends().isEmpty()) {
            for (Long friendId : users.get(id).getFriends()) {
                deleteFromFriends(id, friendId);
            }
        }
        users.remove(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    private void validate(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта должна содержать символ '@'");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователя с ID " + id + " не существует.");
        }

        if (!users.containsKey(friendId)) {
            throw new NotFoundException("Пользователя с ID " + friendId + " не существует.");
        }

        users.get(id).getFriends().add(friendId);
        users.get(friendId).getFriends().add(id);

    }

    @Override
    public void deleteFromFriends(Long id, Long friendId) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователя с ID " + id + " не существует.");
        } else if (!users.containsKey(friendId)) {
            throw new NotFoundException("Пользователя с ID " + friendId + " не существует.");
        }

        users.get(id).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(id);
    }

    @Override
    public Collection<User> getAllFriends(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Указанного пользователя не существует.");
        }

        Collection<User> friendsList = new ArrayList<>();
        for (Long friendId : users.get(id).getFriends()) {
            friendsList.add(users.get(friendId));
        }
        return friendsList;
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long friendId) {
        Collection<User> commonFriendsList = new ArrayList<>(getAllFriends(id));
        commonFriendsList.retainAll(getAllFriends(friendId));
        return commonFriendsList;
    }
}
 */