package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final BookingMapper bookingMapper;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final ItemRepository itemRepository;


    @Override
    public BookingDto addBooking(int userId, BookingDto bookingDto) {

        validateTimeBooking(bookingDto);

        Booking booking = bookingMapper.toBooking(bookingDto);

        User user = userMapper.toUser(userService.getUser(userId));

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не существует"));
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("Вещь недоступна");
        }

        if (item.getOwner().getId() == userId) {
            throw new ObjectNotFoundException("Зачем самому себе брать вещь в аренду! :)");
        }

        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        log.info("Добавлна новый запрос от пользователя: {}", booking.getBooker().getName());

        Booking savedBooking = bookingRepository.save(booking);

        BookingDto result = bookingMapper.toBookingDto(savedBooking);
        result.setItem(itemMapper.toItemDto(item));
        result.setBooker(userMapper.toUserDto(user));

        return result;
    }

    @Override
    public BookingDto approve(int bookingId, int bookerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ObjectNotFoundException("booking не найден"));

        if (booking.getItem().getOwner().getId() != bookerId) {
            throw new OutOfPermissionException("У пользователья нет прав");
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);

        bookingRepository.save(booking);

        return bookingMapper.toBookingDto(booking);

    }

    @Override
    public BookingDto getBooking(int bookingId, int bookerId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ObjectNotFoundException("booking не найден"));

        if (booking.getItem().getOwner().getId() != bookerId || booking.getBooker().getId() != bookerId) {
            return bookingMapper.toBookingDto(booking);
        }
        return null;
    }

    @Override
    public List<BookingDto> getBookingsByUserId(int bookerId, State state) {
        userService.getUser(bookerId);
        return switch (state) {
            case ALL -> bookingMapper.toBookingDto(bookingRepository.findBookingsByBooker_IdOrderByStartDesc(bookerId));
            case CURRENT -> bookingMapper.toBookingDto(bookingRepository.findCurrentActiveBookingsByBookerId(bookerId));
            case PAST -> bookingMapper.toBookingDto(bookingRepository.findPastBookingsByBookerId(bookerId));
            case FUTURE -> bookingMapper.toBookingDto(bookingRepository.findFutureBookingsByBookerId(bookerId));
            case WAITING ->
                    bookingMapper.toBookingDto(bookingRepository.findBookingsByBooker_IdAndStatusOrderByStartDesc(bookerId, Status.WAITING));
            case REJECTED ->
                    bookingMapper.toBookingDto(bookingRepository.findBookingsByBooker_IdAndStatusOrderByStartDesc(bookerId, Status.REJECTED));
        };
    }

    @Override
    public List<BookingDto> getBookingsByOwnerId(int ownerId, State state) {
        userService.getUser(ownerId);
        return switch (state) {
            case ALL -> bookingMapper.toBookingDto(bookingRepository.findBookingsByOwnerId(ownerId));
            case CURRENT -> bookingMapper.toBookingDto(bookingRepository.findCurrentBookingsByOwnerId(ownerId));
            case PAST -> bookingMapper.toBookingDto(bookingRepository.findPastBookingsByOwnerId(ownerId));
            case FUTURE -> bookingMapper.toBookingDto(bookingRepository.findFutureBookingsByOwnerId(ownerId));
            case WAITING ->
                    bookingMapper.toBookingDto(bookingRepository.findAllBookingsByOwnerId(ownerId, Status.WAITING));
            case REJECTED ->
                    bookingMapper.toBookingDto(bookingRepository.findAllBookingsByOwnerId(ownerId, Status.REJECTED));
        };
    }

    private void validateTimeBooking(BookingDto bookingDto) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new ValidationException("Поля не могут быть пустыми");
        }
        if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidationException("Дата начала бронирования не может совпадать с датой окончания!");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new ValidationException("Дата окончания бронирования не может быть в прошлом!!");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new ValidationException("Дата начала бронирования не может быть раньше текущего момента!");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ValidationException("Дата начала бронирования не может быть позднее даты окончания бронирования!");
        }
    }

}
