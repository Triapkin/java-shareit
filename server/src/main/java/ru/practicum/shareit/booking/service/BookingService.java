package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(int userId, BookingDto bookingDto);

    BookingDto approve(int bookingId, int bookerId, boolean approved);

    BookingDto getBooking(int bookingId, int bookerId);

    List<BookingDto> getBookingsByUserId(int bookerId, State state);

    List<BookingDto> getBookingsByOwnerId(int bookerId, State state);

}
