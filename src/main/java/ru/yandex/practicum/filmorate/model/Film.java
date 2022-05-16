package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Data
public class Film {

    private int id;
    @NotNull(message = "Не должно быть null")
    @NotBlank(message = "Не должно быть пустым")
    private String name;
    @NotNull(message = "Не должно быть null")
    @NotBlank(message = "Не должно быть пустым")
    @Size(min = 1, max = 200)
    private String description;
    private LocalDate releaseDate;
    @Min(1)
    private int duration;

}
