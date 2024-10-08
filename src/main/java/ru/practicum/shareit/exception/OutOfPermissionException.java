package ru.practicum.shareit.exception;

public class OutOfPermissionException extends RuntimeException {
    public OutOfPermissionException(String message) {
        super(message);
    }
}
