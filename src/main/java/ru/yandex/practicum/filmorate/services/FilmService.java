package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.UserStorage;
import ru.yandex.practicum.filmorate.validations.FilmNotFoundException;
import ru.yandex.practicum.filmorate.validations.UserNotFoundException;
import ru.yandex.practicum.filmorate.validations.ValidationException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(long userId, long filmId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new ValidationException("нет такого фильма");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new ValidationException("нет такого пользователя");
        }
        Film film = filmStorage.getFilms().get(filmId);
        if(film.getLikes().contains(userId)){
            throw new UserNotFoundException("Уже есть лайки от пользователя");
        }
        film.getLikes().add(userId);
        return filmStorage.update(film);
    }

    public Map<Long, Long> removeLike(long userId, long filmId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new FilmNotFoundException("нет такого фильма");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("нет такого пользователя");
        }
        Film film = filmStorage.getFilms().get(filmId);
        if(!film.getLikes().contains(userId)){
            throw new UserNotFoundException("Нет лайков от этого пользователя");
        }
        film.getLikes().remove(userId);
        filmStorage.update(film);
        return Map.of(userId, filmId);
    }

    public List<Film> top10Films(int count) {
        List<Film> sorted10 = (List<Film>) filmStorage.getFilms().values().stream()
                .sorted(likesComparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
        return sorted10;
    }

    Comparator<Film> likesComparator = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            return o1.getLikes().size() - o2.getLikes().size();
        }
    };



}
