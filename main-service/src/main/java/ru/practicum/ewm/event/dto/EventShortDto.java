package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventShortDto {
    private Integer id;

    @NotNull(message = "Поле annotation не может быть null")
    @NotBlank(message = "Поле annotation не может быть пустым")
    private String annotation;

    @NotNull(message = "Поле category не может быть null")
    @NotBlank(message = "Поле category не может быть пустым")
    private CategoryDto category;

    private Integer confirmedRequests;

    @NotNull(message = "Поле eventDate не может быть null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "Поле initiator не может быть null")
    @NotBlank(message = "Поле initiator не может быть пустым")
    private UserShortDto initiator;

    @NotBlank(message = "Поле paid не может быть пустым")
    private Boolean paid;

    @NotNull(message = "Поле title не может быть null")
    @NotBlank(message = "Поле title не может быть пустым")
    private String title;

    private Integer views;
}
