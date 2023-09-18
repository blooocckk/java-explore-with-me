package ru.practicum.service.event;

import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventOutputDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.EventUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.event.SortType;
import ru.practicum.model.event.State;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventOutputDto create(EventDto eventDto, Long userId);

    EventOutputDto getByCreator(Long eventId, Long userId);

    List<EventShortDto> getAlLByCreator(Long userId, Integer from, Integer size);

    EventOutputDto updateByCreator(EventUpdateRequest eventUpdateRequest, Long eventId, Long userId);

    List<EventOutputDto> search(List<Long> users, List<State> states, List<Long> categories,
                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventOutputDto updateByAdmin(EventUpdateRequest eventUpdateRequest, Long eventId);

    List<EventShortDto> searchWithFilters(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Boolean onlyAvailable, SortType sort, Integer from,
                                          Integer size, HttpServletRequest request);

    EventOutputDto get(Long eventId, HttpServletRequest request);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
