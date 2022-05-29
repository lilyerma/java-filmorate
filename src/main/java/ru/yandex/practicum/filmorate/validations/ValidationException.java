package ru.yandex.practicum.filmorate.validations;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Ошибка валидации")
public class ValidationException extends RuntimeException {
    public ValidationException (final String message) {
        super(message);
    }
}
