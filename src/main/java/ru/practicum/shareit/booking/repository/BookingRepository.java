package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findBookingsByBooker_IdOrderByStartDesc(int bookerId);

    @Query("SELECT b FROM Booking  b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.start <= CURRENT_TIMESTAMP " +
            "AND b.end >= CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentActiveBookingsByBookerId(int bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.end <= CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookingsByBookerId(int bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.end <= CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<Booking> findFutureBookingsByBookerId(int bookerId);

    List<Booking> findBookingsByBooker_IdAndStatusOrderByStartDesc(int bookerId, BookingStatus status);

    @Query("SELECT (COUNT(b) > 0) FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.item.id = :itemId " +
            "AND b.end <= CURRENT_TIMESTAMP")
    boolean existsByBookerIdAndItemIdPast(long bookerId, long itemId);

    @Query(value = "SELECT b FROM Booking b " +
            "JOIN Item i ON i.id = b.item.id  " +
            "WHERE i.owner.id = :ownerId " +
            "ORDER BY b.start DESC")
    List<Booking> findBookingsByOwnerId(int ownerId);

    @Query(value = "SELECT b FROM Booking b " +
            "JOIN Item i ON i.id = b.item.id " +
            "WHERE i.owner.id = :ownerId " +
            "AND b.start <= CURRENT_TIMESTAMP " +
            "AND b.end >= CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentBookingsByOwnerId(int ownerId);

    @Query(value = "SELECT b FROM Booking b " +
            "JOIN Item i ON i.id = b.item.id " +
            "WHERE i.owner.id = :ownerId " +
            "AND b.end <= CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookingsByOwnerId(int ownerId);

    @Query(value = "SELECT b FROM Booking b " +
            "JOIN Item i ON i.id = b.item.id " +
            "WHERE i.owner.id = :ownerId " +
            "AND b.start >= CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<Booking> findFutureBookingsByOwnerId(int ownerId);

    @Query(value = "SELECT b FROM Booking b " +
            "JOIN Item i ON i.id = b.item.id " +
            "WHERE i.owner.id = :ownerId " +
            "AND b.status = :status " +
            "AND b.start>= CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<Booking> findAllBookingsByOwnerId(int ownerId, BookingStatus status);

}