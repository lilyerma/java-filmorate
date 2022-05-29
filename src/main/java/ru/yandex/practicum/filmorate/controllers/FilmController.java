package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.validations.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final LocalDate DATE = LocalDate.of(1895, 12, 28);
    private FilmStorage filmStorage;
    private FilmService filmService;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService){
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmStorage.getFilms().values().stream().collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public Film getById(@PathVariable Long id) {
        return filmStorage.getByID(id);
    }

    @PostMapping
    public Film create(@Valid @NotNull @RequestBody Film film) {
        validateFilm(film);
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@Valid @NotNull @RequestBody Film film) {
        validateFilm(film);
        return filmStorage.update(film);
    }

    @DeleteMapping
    public Film remove(@Valid @NotNull @RequestBody Film film) {
        return filmStorage.remove(film);
    }

    @PutMapping("{id}/like/{userId}")
    public Film addLike(
            @PathVariable Long id,
            @PathVariable Long userId
    ){
        return filmService.addLike(userId, id);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Map<Long, Long> removeLike(
            @PathVariable Long id,
            @PathVariable Long userId
    ){
        return filmService.removeLike(userId,id);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count){
    return filmService.top10Films(count);
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
