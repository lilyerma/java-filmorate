package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validations.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
public class FilmController {

    private final LocalDate DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<Integer, Film>();
 //   private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private int counter = 0;


    @GetMapping("/films")
    public List<Film> findAll() {
        return new ArrayList<Film>(films.values());
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @NotNull@RequestBody Film film) {
        log.debug("Такой фильм: " + film);
            validateFilm(film);
            film.setId(counter + 1);
            counter+=1;
            films.put(film.getId(), film);
            log.debug("Добавил фильм " + film);
            return film;
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @NotNull @RequestBody Film film)  {
        log.debug("Такой фильм: " + film);
            validateFilm(film);
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.debug("Обновил фильм " + film);
                return film;
            } else {
                log.debug("Нет фильма с таким ID " + film);
                throw new ValidationException("Нет фильма для обновления");
            }
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> exceptionHandler(ValidationException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public void validateFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(DATE)) {
            throw new ValidationException("релиз раньше 1895 года");
        }
    }

}
