package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    private final UserController userController = new UserController();

    @Test
    void shouldThrowExceptionIfEmailIsInvalid() {
        User invalidUser = new User();
        invalidUser.setEmail("invalid-email");
        invalidUser.setLogin("login");
        invalidUser.setBirthday(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () ->
                userController.addUser(invalidUser));
        assert exception.getMessage().equals("Электронная почта должна содержать символ '@'");
    }

    @Test
    void shouldThrowExceptionIfLoginContainsSpaces() {
        User invalidUser = new User();
        invalidUser.setEmail("user@example.com");
        invalidUser.setLogin("login with spaces");
        invalidUser.setBirthday(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () ->
                userController.addUser(invalidUser));
        assert exception.getMessage().equals("Логин не может содержать пробелы");
    }

    @Test
    void shouldThrowExceptionIfBirthDayIsInTheFuture() {
        User invalidUser = new User();
        invalidUser.setEmail("user@example.com");
        invalidUser.setLogin("login");
        invalidUser.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception = assertThrows(ValidationException.class, () ->
                userController.addUser(invalidUser));
        assert exception.getMessage().equals("Дата рождения не может быть в будущем");
    }

    @Test
    void shouldPassValidationForValidUser() {
        User validUser = new User();
        validUser.setEmail("user@example.com");
        validUser.setLogin("login");
        validUser.setBirthday(LocalDate.of(2000, 1, 1));

        User addedUser = userController.addUser(validUser);

        assert addedUser != null;
        assert addedUser.getId() != null;
    }
}