package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validations.ValidationException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {

    private final LocalDate DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<Integer, Film>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private int counter = 0;


    @GetMapping("/films")
    public HashMap<Integer, Film> findAll() {
        log.debug("Текущее количество фильмов: " + films.size());
        return (HashMap<Integer, Film>) films;
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        log.debug("Такой фильм: " + film);
        try {
            validateFilm(film);
            film.setId(counter + 1);
            films.put(film.getId(), film);
            log.debug("Добавил фильм " + film);
            return film;
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) throws ValidationException {
        log.debug("Такой фильм: " + film);
        try {
            validateFilm(film);
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.debug("Обновил фильм " + film);
                return film;
            } else {
                log.debug("Нет фильма с таким ID " + film);
                throw new ValidationException("Нет фильма для обновления");
            }
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            throw e;
        }

    }


    public void validateFilm(Film film) throws ValidationException {
        if (film.getName().isBlank() || film.getName() == null) {
            throw new ValidationException("пустое имя фильма");
        } else if (film.getDescription().length() > 200 ||film.getDescription().isEmpty() ) {
            throw new ValidationException("длина описания больше 200");
        } else if (film.getReleaseDate().isBefore(DATE)) {
            throw new ValidationException("релиз раньше 1895 года");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("длина фильма не может быть меньше 0");
        }
    }

}
