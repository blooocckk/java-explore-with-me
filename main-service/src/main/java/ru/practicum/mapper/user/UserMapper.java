package ru.practicum.mapper.user;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserOutputDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.user.User;

@UtilityClass
public class UserMapper {
    public UserDto toDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserOutputDto toOutputDto(User user) {
        return UserOutputDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserShortDto toShortDto(User user) {
        return UserShortDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toEntity(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
