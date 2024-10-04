package ru.practicum.shareit.request.model;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequest {
    int id;
    String description;
    User requestor;
    LocalDate created;
}
