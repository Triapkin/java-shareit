package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;
    private static final String HEADER_PREFIX = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestHeader(HEADER_PREFIX) int userId,
            @RequestBody @Valid ItemDto itemDto) {
        log.info("Запрос на создание вещи пользователем с ID {}: {}", userId, itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @RequestHeader(HEADER_PREFIX) int userId,
            @PathVariable int itemId,
            @RequestBody ItemDto itemDto) {
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(
            @PathVariable int itemId) {
        return itemClient.getItemById(itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            @RequestParam("text") String searchText) {
        return itemClient.search(searchText);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable int itemId,
                                             @RequestBody @Valid CommentDto commentDto,
                                             @RequestHeader(HEADER_PREFIX) @NotNull int userId) {
        return itemClient.addComment(itemId, commentDto, userId);
    }
}
