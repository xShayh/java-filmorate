package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {
    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(min = 1, max = 200, message = "Описание фильма не может превышать 200 символов")
    private String description;
    @NotNull(message = "Дата релиза не может быть пустой")
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    private Date releaseDate;
    @Positive(message = "Продолжительность фильма должна быть больше 0")
    private int duration;
    private Set<Long> likes = new HashSet<>();
    private List<Genre> genres;
    @NotNull
    private Mpa mpa;

    public Film(@NonNull String name, String description, Date releaseDate, Integer duration, @NotNull Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
}
