package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestDto {
    private int id;
    private String description;
    private List<Item> items;
    private LocalDateTime created;
}
