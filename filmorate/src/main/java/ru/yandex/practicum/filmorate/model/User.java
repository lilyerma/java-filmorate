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
public class User {

    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

}
