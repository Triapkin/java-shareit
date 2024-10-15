package ru.practicum.shareit.booking.model;

public enum Status {
    WAITING, // Ожидание подтверждения владельцем
    APPROVED, // Подтверждено владельцем
    REJECTED, // Отклонено владельцем
    CANCELED  // Отменено пользователем
}
