package ru.practicum.ewm.like.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewLikeDto {
    @NotNull(message = "Поле liked не может быть null")
    private Boolean liked;

    @NotNull(message = "Поле user не может быть null")
    private Integer user;

    @NotNull(message = "Поле event не может быть null")
    private Integer event;
}
