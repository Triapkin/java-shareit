package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    List<ItemRequestDto> toItemRequestDto(List<ItemRequest> itemRequests);

    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);

    List<ItemRequest> toItemRequest(List<ItemRequestDto> itemRequestsDto);
}