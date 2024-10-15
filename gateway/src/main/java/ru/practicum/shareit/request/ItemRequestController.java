package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final RequestClient requestClient;
    private static final String HEADER_PREFIX = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader(HEADER_PREFIX) int userId,
                                             @RequestBody ItemRequestDto dto) {
        return requestClient.addRequest(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> getByUserId(@RequestHeader(HEADER_PREFIX) int userId) {
        log.info("Получен запрос к эндпоинту /requests getForUser с headers {}", userId);
        return requestClient.getByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable int requestId) {
        return requestClient.getRequestById(requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getALlRequests() {
        return requestClient.getAllRequests();
    }

}
