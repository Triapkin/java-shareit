package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("SELECT i FROM Item i WHERE " +
            "(LOWER(i.name) LIKE %:text% OR LOWER(i.description) LIKE %:text%) " +
            "AND i.available = true")
    List<Item> searchByNameOrDescription(@Param("text") String text);

    List<Item> findAllByOwnerId(int ownerId);

    List<Item> findAllByRequestId(int requestId);
}
