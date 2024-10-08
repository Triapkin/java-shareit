package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(int userId, ItemDto itemDto);

    ItemDto updateItem(int userId, int itemId, ItemDto itemDto);

    ItemDto getItemById(int itemId);

    List<ItemDto> getAllItemsByUserId(int userId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(int itemId, CommentDto commentDto, int userId);
}
