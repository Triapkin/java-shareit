package ru.practicum.shareit.item.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends CrudRepository<Item, Integer> {
}
