package ru.yandex.practicum.filmorate.validations;

public class FilmNotFoundException extends RuntimeException{
    private final String parameter;
    public FilmNotFoundException (String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
