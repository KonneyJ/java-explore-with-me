package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.enums.EventState;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {
    private Integer id;

    @NotNull(message = "Поле annotation не может быть null")
    @NotBlank(message = "Поле annotation не может быть пустым")
    private String annotation;

    @NotNull(message = "Поле category не может быть null")
    @NotBlank(message = "Поле category не может быть пустым")
    private CategoryDto category;

    private Integer confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @NotNull(message = "Поле eventDate не может быть null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "Поле initiator не может быть null")
    @NotBlank(message = "Поле initiator не может быть пустым")
    private UserShortDto initiator;

    @NotNull(message = "Поле location не может быть null")
    @NotBlank(message = "Поле location не может быть пустым")
    private Location location;

    @NotBlank(message = "Поле paid не может быть пустым")
    private Boolean paid;

    private Integer participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private EventState state;

    @NotNull(message = "Поле title не может быть null")
    @NotBlank(message = "Поле title не может быть пустым")
    private String title;

    private Integer views;
}
