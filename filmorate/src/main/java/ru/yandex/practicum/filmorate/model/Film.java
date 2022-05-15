package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@Getter
@Setter
public class Film {

    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

}
