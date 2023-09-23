package ru.practicum.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exception.ObjectAlreadyExistsException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.mapper.request.RequestMapper;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;
import ru.practicum.model.request.Request;
import ru.practicum.model.user.User;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.request.RequestRepository;
import ru.practicum.repository.user.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event not found"));

        if (requestRepository.existsByEvent_IdAndRequester_Id(eventId, userId)) {
            throw new ObjectAlreadyExistsException("The request already exists");
        }

        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ObjectAlreadyExistsException("You cannot send a participation request for your own event");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ObjectAlreadyExistsException("You do not have access to this event");
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ObjectAlreadyExistsException("The participation limit has been exceeded");
        }

        Request request = Request.builder()
                .requester(user)
                .event(event)
                .build();

        if (event.isRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(State.PENDING);
        } else {
            request.setStatus(State.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        Request savedRequest = requestRepository.save(request);
        log.info("Participation request created successfully: Request ID - {}", savedRequest.getId());
        return RequestMapper.toParticipationRequestDto(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getAll(Long userId) {
        List<ParticipationRequestDto> requestDtoList = requestRepository.findByRequester_Id(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        log.info("Retrieved {} participation requests for User ID - {}", requestDtoList.size(), userId);
        return requestDtoList;
    }

    @Override
    @Transactional
    public ParticipationRequestDto update(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Request not found"));

        if (request.getRequester().getId().equals(userId)) {
            request.setStatus(State.CANCELED);
        } else {
            throw new ObjectAlreadyExistsException("You do not have access to this event");
        }

        Request savedRequest = requestRepository.save(request);
        log.info("Participation request updated successfully: Request ID - {}", savedRequest.getId());
        return RequestMapper.toParticipationRequestDto(savedRequest);
    }
}
