package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequestDto {
    int id;
    @NotBlank
    String description;
    User requestor;
    @NotNull(message = "Дата создания не может быть пустой")
    @JsonFormat(pattern = ("yyyy-MM-dd"))
    LocalDate created;
}
