package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public ItemDto createItem(int userId, ItemDto itemDto) {
        userService.getUserById(userId);
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(userRepository.findById(userId).get());

        log.info("Предмет создан: {}", item);

        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(int userId, int itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Итем не найден"));

        if (item == null) {
            throw new NotFoundException("Предмет с ID " + itemId + " не найден");
        }

        if (item.getOwner().getId() != userId) {
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

        itemRepository.save(item);
        log.info("Предмет обновлен: {}", item);

        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(int itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет с ID " + itemId + " не найден"));
        ItemDto itemDto = itemMapper.toItemDto(item);
        itemDto.setComments(commentRepository.getCommentsTextByItemId(itemId));
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(int userId) {
        userService.getUserById(userId);
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) return List.of();
        return itemRepository.searchByNameOrDescription(text.toLowerCase()).stream()
                .filter(Item::getAvailable)
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(int itemId, CommentDto commentDto, int userId) {
        if (!bookingRepository.existsByBookerIdAndItemIdPast(userId, itemId)) {
            throw new ItemNotAvailableException("Not allowed for current booking");
        }

        Comment comment = new Comment();

        comment.setItem(itemMapper.toItem(getItemById(itemId)));
        comment.setAuthor(userRepository.findById(userId).get());
        comment.setText(commentDto.getText());
        comment.setCreated(LocalDateTime.now());
        comment = commentRepository.save(comment);

        return commentMapper.toCommentDto(comment);
    }
}
