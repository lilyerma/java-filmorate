package ru.yandex.practicum.filmorate.storages;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validations.FilmNotFoundException;
import ru.yandex.practicum.filmorate.validations.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<Long, Film>();
    private long counter = 0;

    @Override
    public HashMap<Long, Film> getFilms(){
        return (HashMap<Long, Film>) films;
    }

    @Override
    public Film getByID(Long id) {
        if(!films.containsKey(id)){
            throw new FilmNotFoundException("Нет такого кино");
        }
        return films.get(id);
    }


    @Override
    public Film create(Film film) {
        film.setId(counter + 1);
        counter+=1;
        films.put((long) counter, film);
        return film;
    }

    @Override
    public Film update(Film film)  {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new FilmNotFoundException("Нет фильма для обновления");
        }
    }

    @Override
    public Film remove(Film film)  {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Нет фильма для удаления");
        }
    }

}
