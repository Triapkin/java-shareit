package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    List<UserDto> toUserDto(List<User> users);

    User toUser(UserDto userDto);

    List<User> toUser(List<UserDto> usersDto);
}
