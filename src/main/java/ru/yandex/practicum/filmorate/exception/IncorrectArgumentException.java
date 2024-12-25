package ru.yandex.practicum.filmorate.exception;

public class IncorrectArgumentException extends IllegalArgumentException {
    public IncorrectArgumentException(String message) {
        super(message);
    }
}