package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryUserStorageTest {

    private InMemoryUserStorage userStorage;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
    }

    @Test
    void shouldAddAndRetrieveUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test_user");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        userStorage.add(user);
        User retrievedUser = userStorage.get(user.getId());

        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(retrievedUser.getLogin()).isEqualTo("test_user");
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        User user = new User();
        user.setEmail("invalid_email");
        user.setLogin("user123");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userStorage.add(user));
    }

    @Test
    void shouldAddAndRetrieveFriends() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        userStorage.add(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
        userStorage.add(user2);

        userStorage.addFriend(user1.getId(), user2.getId());

        Collection<User> friendsOfUser1 = userStorage.getAllFriends(user1.getId());
        assertThat(friendsOfUser1).hasSize(1);
        assertThat(friendsOfUser1.iterator().next().getEmail()).isEqualTo("user2@example.com");

        Collection<User> friendsOfUser2 = userStorage.getAllFriends(user2.getId());
        assertThat(friendsOfUser2).hasSize(1);
        assertThat(friendsOfUser2.iterator().next().getEmail()).isEqualTo("user1@example.com");
    }
}
