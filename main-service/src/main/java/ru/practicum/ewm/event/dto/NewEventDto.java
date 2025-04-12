package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.model.Location;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {
    @NotNull(message = "Поле annotation не может быть null")
    @NotBlank(message = "Поле annotation не может быть пустым")
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull(message = "Поле category не может быть null")
    @NotBlank(message = "Поле category не может быть пустым")
    private int category;

    @NotNull(message = "Поле description не может быть null")
    @NotBlank(message = "Поле description не может быть пустым")
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull(message = "Поле eventDate не может быть null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "Поле location не может быть null")
    @NotBlank(message = "Поле location не может быть пустым")
    private Location location;

    private boolean paid;

    @PositiveOrZero
    private int participantLimit;

    private boolean requestModeration;

    @NotNull(message = "Поле title не может быть null")
    @NotBlank(message = "Поле title не может быть пустым")
    @Size(min = 3, max = 120)
    private String title;
}
