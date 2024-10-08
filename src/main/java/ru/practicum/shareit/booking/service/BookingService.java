package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(int userId, BookingDto bookingDto);

    BookingDto approve(int bookingId, int bookerId, boolean approved);

    BookingDto getBooking(int bookingId, int bookerId);

    List<BookingDto> getBookingsByUserId(int bookerId, BookingState state);

    List<BookingDto> getBookingsByOwnerId(int bookerId, BookingState state);
}
