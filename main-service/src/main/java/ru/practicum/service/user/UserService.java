package ru.practicum.service.user;

import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserOutputDto;

import java.util.List;

public interface UserService {
    UserOutputDto create(UserDto userDto);

    List<UserOutputDto> get(List<Long> ids, Integer from, Integer size);

    void delete(Long userId);
}
