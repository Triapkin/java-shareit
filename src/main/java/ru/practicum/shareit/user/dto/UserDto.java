package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private int id;
    private String name;

    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    @NotBlank(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;
}
