package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info("Запрос на создание пользователя: {}", userDto);
        return userServiceImpl.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable("id") int id, @RequestBody UserDto userDto) {
        log.info("Запрос на обновление пользователя с ID {}: {}", id, userDto);
        return userServiceImpl.updateUser(id, userDto);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") int id) {
        log.info("Запрос на получение пользователя с ID: {}", id);
        return userServiceImpl.getUser(id);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        log.info("Запрос на получение всех пользователей");
        return userServiceImpl.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int id) {
        log.info("Запрос на удаление пользователя с ID: {}", id);
        userServiceImpl.deleteUser(id);
    }
}
