package ru.yandex.practicum.filmorate.validations;

public class UserNotFoundException extends RuntimeException{
    private final String parameter;
    public UserNotFoundException (String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
