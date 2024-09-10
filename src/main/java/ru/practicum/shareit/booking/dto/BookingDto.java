package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
public class BookingDto {
    int id;
    @NotNull(message = "Дата старта не может быть пустой")
    @JsonFormat(pattern = ("yyyy-MM-dd"))
    LocalDate start;
    @NotNull(message = "Дата конца не может быть пустой")
    @JsonFormat(pattern = ("yyyy-MM-dd"))
    LocalDate end;
    Item item;
    User booker;
    BookingStatus status;
}
