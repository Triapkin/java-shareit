package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto requestDto) {
        log.info("Creating client {}", requestDto);
        return userClient.postCreateUser(requestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable int id, @RequestBody UserDto requestDto) {
        log.info("Updating client {}", requestDto);
        return userClient.patchUpdateUser(id, requestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable int id) {
        log.info("Getting client {}", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Getting client users");
        return userClient.getUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") int id) {
        log.info("Deleting client {}", id);
        return userClient.deleteUser(id);
    }
}
