package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Map<Integer, User> users = new HashMap<>();
    private final UserMapper userMapper;

    private int userIdCounter = 1;

    @Override
    public UserDto createUser(UserDto userDto) {

        if (isEmailAlreadyUsed(userDto.getEmail(), -1)) {
            throw new IllegalArgumentException("Email уже используется: " + userDto.getEmail());
        }
        User user = userMapper.toUser(userDto);
        user.setId(userIdCounter++);

        users.put(user.getId(), user);
        log.info("Пользователь создан: {}", user);

        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(int id, UserDto userDto) {
        UserDto user = getUserById(id);
        user.setName(userDto.getName());

        if (userDto.getEmail() != null && isEmailAlreadyUsed(userDto.getEmail(), id)) {
            throw new IllegalArgumentException("Email уже используется: " + userDto.getEmail());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        users.put(id, userMapper.toUser(user));
        log.info("Пользователь обновлен: {}", user);

        return user;
    }

    @Override
    public UserDto getUserById(int id) {
        User user = findUser(id);
        log.info("Пользователь получен по ID: {}", id);
        return userMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = new ArrayList<>(users.values());
        log.info("Получение всех пользователей, количество: {}", userList.size());
        return userMapper.toUserDto(userList);
    }

    @Override
    public void deleteUser(int id) {
        getUserById(id);
        users.remove(id);
        log.info("Пользователь удален с ID: {}", id);
    }

    private User findUser(int id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return user;
    }

    private boolean isEmailAlreadyUsed(String email, int currentUserId) {
        return users.values().stream()
                .anyMatch(user -> email.equals(user.getEmail()) && user.getId() != currentUserId);
    }
}
