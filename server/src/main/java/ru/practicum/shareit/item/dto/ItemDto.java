package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private int id;
    @Size(min = 1, max = 50, message = "Максимальная длина имени — 50 символов.")
    @NotBlank(message = "name не может быть пустым")
    private String name;
    @Size(min = 1, max = 255, message = "Максимальная длина описания — 255 символов.")
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    @NotNull
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<String> comments;
    private int requestId;
}
