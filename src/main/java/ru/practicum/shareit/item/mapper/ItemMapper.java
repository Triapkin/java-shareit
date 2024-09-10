package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto toItemDto(Item item);

    List<ItemDto> toItemDto(List<Item> items);

    Item toItem(ItemDto itemDto);

    List<Item> toItems(List<ItemDto> itemsDto);
}
