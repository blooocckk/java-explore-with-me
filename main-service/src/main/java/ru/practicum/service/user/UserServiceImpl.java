package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserOutputDto;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.model.user.User;
import ru.practicum.repository.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserOutputDto create(UserDto userDto) {
        User user = userRepository.save(UserMapper.toEntity(userDto));
        log.info("User created successfully: User ID - {}", user.getId());
        return UserMapper.toOutputDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserOutputDto> get(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<UserOutputDto> userOutputDtoList = userRepository.getUsersByIds(ids, pageable);
        log.info("Returning a list of {} users", userOutputDtoList.size());
        return userOutputDtoList;
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
        log.info("User deleted successfully: User ID - {}", userId);
    }
}