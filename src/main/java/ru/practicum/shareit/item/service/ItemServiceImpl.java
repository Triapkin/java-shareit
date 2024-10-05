package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto createItem(int userId, ItemDto itemDto) {
        userService.getUserById(userId);
        itemDto.setOwnerId(userId);
        itemRepository.save(itemMapper.toItem(itemDto));
        log.info("Предмет создан: {}", itemDto);
        return itemDto;
    }

    @Override
    public ItemDto updateItem(int userId, int itemId, ItemDto itemDto) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new NotFoundException("Предмет с ID " + itemId + " не найден");
        }

        if (item.getOwnerId() != userId) {
            throw new NotFoundException("Пользователь с ID " + userId + " не имеет прав на обновление этого предмета");
        }

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        items.put(itemId, item);
        log.info("Предмет обновлен: {}", item);

        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(int itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет с ID " + itemId + " не найден"));
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(int userId) {
        userService.getUserById(userId);
        itemRepository.findAllByOwnerId();
        return itemRepository.findAll().stream()
                .filter(item -> item.get == userId)
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) return List.of();
        return items.values().stream()
                .filter(item -> item.getAvailable() != null && item.getAvailable() &&
                        (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
