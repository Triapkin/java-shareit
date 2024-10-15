package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDto toBookingDto(Booking booking);

    List<BookingDto> toBookingDto(List<Booking> bookings);

    Booking toBooking(BookingDto bookingDto);

    List<Booking> toBooking(List<BookingDto> bookingsDto);
}
