package ru.practicum.shareit.request.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/requests")
public class ItemRequestСontroller {

    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                     @RequestBody ItemRequestDto dto) {
        return requestService.addRequest(userId, dto);
    }

    @GetMapping
    public List<ItemRequestDto> getByUserId(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получен запрос к эндпоинту /requests getForUser с headers {}", userId);
        return requestService.getByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable int requestId) {
        return requestService.getRequestById(requestId);
    }
}