package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemServiceImpl itemServiceImpl;

    @PostMapping
    public ItemDto createItem(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody @Valid ItemDto itemDto) {
        log.info("Запрос на создание вещи пользователем с ID {}: {}", userId, itemDto);
        return itemServiceImpl.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @PathVariable int itemId,
            @RequestBody ItemDto itemDto) {
        return itemServiceImpl.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(
            @PathVariable int itemId) {
        return itemServiceImpl.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUser(
            @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemServiceImpl.getAllItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(
            @RequestParam("text") String searchText) {
        return itemServiceImpl.searchItems(searchText);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable int itemId,
                                 @RequestBody @Valid CommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemServiceImpl.addComment(itemId, commentDto, userId);
    }

}
