package ru.practicum.service.request;

import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto create(Long userId, Long eventId);

    List<ParticipationRequestDto> getAll(Long userId);

    ParticipationRequestDto update(Long userId, Long requestId);
}
