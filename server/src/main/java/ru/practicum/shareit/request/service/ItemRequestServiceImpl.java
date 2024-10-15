package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserService userService;
    private final ItemRequestMapper itemRequestMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto addRequest(int userId, ItemRequestDto requestDto) {

        userService.getUser(userId);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(requestDto);
        itemRequest.setRequestor(userRepository.findById(userId).get());
        itemRequest.setCreated(LocalDateTime.now());

        requestRepository.save(itemRequest);

        return itemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getByUserId(int requestorId) {
        List<ItemRequest> itemRequests = requestRepository.findAllByRequestorId(requestorId);

        return itemRequests.stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequestById(int requestId) {
        ItemRequest itemRequest = requestRepository.findById(requestId).get();
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(itemRepository.findAllByRequestId(requestId));

        return itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> getALlRequests() {
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.findAllByOrderByCreatedDesc());
    }
}
