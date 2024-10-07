package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OutOfPermissionException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingMapper bookingMapper;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Override
    public BookingDto addBooking(int userId, BookingDto bookingDto) {
        validateTimeBooking(bookingDto);
        Booking booking = bookingMapper.toBooking(bookingDto);

        User user = userMapper.toUser(userService.getUserById(userId));

        Item item = itemMapper.toItem(itemService.getItemById(bookingDto.getItemId()));

        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("Вещь недоступна");
        }

        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        log.info("Добавлна новый запрос от пользователя: {}", booking.getBooker().getName());

        Booking savedBooking = bookingRepository.save(booking);

        BookingDto result = bookingMapper.toBookingDto(savedBooking);
        result.setItem(itemMapper.toItemDto(item));
        result.setBooker(userMapper.toUserDto(user));

        return result;
    }

    @Override
    public BookingDto approve(int bookingId, int bookerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("booking не найден"));

        if (booking.getItem().getOwner().getId() != bookerId) {
            throw new OutOfPermissionException("У пользователья нет прав");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        bookingRepository.save(booking);

        return bookingMapper.toBookingDto(booking);

    }

    @Override
    public BookingDto getBooking(int bookingId, int bookerId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("booking не найден"));

        if (booking.getItem().getOwner().getId() != bookerId || booking.getBooker().getId() != bookerId) {
            return bookingMapper.toBookingDto(booking);
        }
        return null;
    }

    @Override
    public List<BookingDto> getBookingsByUserId(int bookerId, BookingState state) {
        userService.getUserById(bookerId);
        return switch (state) {
            case ALL -> bookingMapper.toBookingDto(bookingRepository.findBookingsByBooker_IdOrderByStartDesc(bookerId));
            case CURRENT -> bookingMapper.toBookingDto(bookingRepository.findCurrentActiveBookingsByBookerId(bookerId));
            case PAST -> bookingMapper.toBookingDto(bookingRepository.findPastBookingsByBookerId(bookerId));
            case FUTURE -> bookingMapper.toBookingDto(bookingRepository.findFutureBookingsByBookerId(bookerId));
            case WAITING ->
                    bookingMapper.toBookingDto(bookingRepository.findBookingsByBooker_IdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING));
            case REJECTED ->
                    bookingMapper.toBookingDto(bookingRepository.findBookingsByBooker_IdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED));
        };
    }

    @Override
    public List<BookingDto> getBookingsByOwnerId(int ownerId, BookingState state) {
        userService.getUserById(ownerId);
        return switch (state) {
            case ALL -> bookingMapper.toBookingDto(bookingRepository.findBookingsByOwnerId(ownerId));
            case CURRENT -> bookingMapper.toBookingDto(bookingRepository.findCurrentBookingsByOwnerId(ownerId));
            case PAST -> bookingMapper.toBookingDto(bookingRepository.findPastBookingsByOwnerId(ownerId));
            case FUTURE -> bookingMapper.toBookingDto(bookingRepository.findFutureBookingsByOwnerId(ownerId));
            case WAITING ->
                    bookingMapper.toBookingDto(bookingRepository.findAllBookingsByOwnerId(ownerId, BookingStatus.WAITING));
            case REJECTED ->
                    bookingMapper.toBookingDto(bookingRepository.findAllBookingsByOwnerId(ownerId, BookingStatus.REJECTED));
        };
    }

    private void validateTimeBooking(BookingDto bookingDto) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new ValidationException("Поля не могут быть пустыми");
        }
        if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidationException("Дата начала бронирования не может совпадать с датой окончания!");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ValidationException("Дата начала бронирования не может быть позднее даты окончания бронирования!");
        }
    }
}
