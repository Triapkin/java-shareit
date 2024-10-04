package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Item {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private int ownerId;
    private int requestId;
}
