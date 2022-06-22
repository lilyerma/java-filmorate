package ru.yandex.practicum.filmorate.model;

import lombok.Data;


import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {


    private long id;
    private Set<Long> friends = new HashSet<>();
    @Email
    private String email;
    @NotNull(message = "Не должно быть null")
    @NotBlank(message = "Не должно быть пустым")
    @Pattern(regexp = "^\\S+$")
    private String login;
    private String name;
    @NotNull(message = "Нужна дата рождения.")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;

}
