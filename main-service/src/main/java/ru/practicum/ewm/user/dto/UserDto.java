package ru.practicum.ewm.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private int id;

    @NotNull(message = "Поле name не может быть null")
    @NotBlank(message = "Поле name не может быть пустым")
    private String name;

    @NotNull(message = "Поле email не может быть null")
    @NotBlank(message = "Поле email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;
}
