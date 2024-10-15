package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto addBookingRequest(@RequestBody BookingDto bookingDto,
                                        @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Добавлен новый запрос: {}", bookingDto);
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@PathVariable int bookingId,
                              @RequestHeader("X-Sharer-User-Id") int bookerId,
                              @RequestParam Boolean approved) {
        return bookingService.approve(bookingId, bookerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int bookerId) {
        return bookingService.getBooking(bookingId, bookerId);
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") int bookerId,
                                        @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingsByUserId(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                               @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingsByOwnerId(ownerId, state);
    }
}
