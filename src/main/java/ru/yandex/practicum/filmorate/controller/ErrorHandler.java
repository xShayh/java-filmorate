package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    public Map<String, String> handleIncorrectArgument(final IncorrectArgumentException e) {
        log.info("IllegalArgumentException {}", e.getMessage());
        return Map.of("Передан неверный аргумент", e.getMessage());
    }

    @ExceptionHandler
    public Map<String, String> handleError(final RuntimeException e) {
        log.info("RuntimeException {}", e.getMessage());
        return Map.of("Произошла ошибка", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationError(final ValidationException e) {
        log.info("ERROR 400 {}", e.getMessage());
        return Map.of("Произошла ошибка валидации", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundError(final NotFoundException e) {
        log.info("ERROR 404 {}", e.getMessage());
        return Map.of("Объект не найден", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalServerError(final HttpServerErrorException.InternalServerError e) {
        log.info("ERROR 500 {}", e.getMessage());
        return Map.of("Произошла внутренняя ошибка сервера", e.getMessage());
    }
}