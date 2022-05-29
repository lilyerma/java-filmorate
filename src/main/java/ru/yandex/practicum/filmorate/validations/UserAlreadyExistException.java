package ru.yandex.practicum.filmorate.validations;

public class UserAlreadyExistException extends RuntimeException {
    private final String parameter;

    public UserAlreadyExistException (String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
