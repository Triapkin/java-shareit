package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addRequest(int userId, ItemRequestDto requestDto);

    List<ItemRequestDto> getByUserId(int userId);

    ItemRequestDto getRequestById(int requestId);

    List<ItemRequestDto> getALlRequests();
}
