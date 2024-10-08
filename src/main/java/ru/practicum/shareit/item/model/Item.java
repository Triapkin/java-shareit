package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    @Column(name = "is_available")
    private Boolean available;
    @JoinColumn(name = "owner_id")
    @ManyToOne
    private User owner;
    @JoinColumn(name = "request_id")
    private int requestId;
}
