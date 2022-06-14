package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Film remove(Film film);

    HashMap<Long, Film> getFilms();

    boolean equals(Object o);


    int hashCode();

    String toString();

    Film getByID(Long id);
}
