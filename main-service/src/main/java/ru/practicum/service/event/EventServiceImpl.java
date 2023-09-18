package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Constants;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ViewStats;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventOutputDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.EventUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exception.ObjectAlreadyExistsException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.mapper.location.LocationMapper;
import ru.practicum.mapper.request.RequestMapper;
import ru.practicum.model.ModuleInfo;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.*;
import ru.practicum.model.pagination.PageCalculation;
import ru.practicum.model.request.Request;
import ru.practicum.model.user.User;
import ru.practicum.repository.category.CategoryRepository;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.event.EventSpecifications;
import ru.practicum.repository.location.LocationRepository;
import ru.practicum.repository.request.RequestRepository;
import ru.practicum.repository.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;
    private final ModuleInfo moduleInfo;

    private void validateEventDto(EventDto eventDto) {
        if (eventDto.getPaid() == null) {
            eventDto.setPaid(false);
        }
        if (eventDto.getParticipantLimit() == null) {
            eventDto.setParticipantLimit(0L);
        }
        if (eventDto.getRequestModeration() == null) {
            eventDto.setRequestModeration(true);
        }
    }

    @Transactional
    private void saveStats(String requestURI, String remoteAddr) {
        EndpointHitDto hit = new EndpointHitDto(moduleInfo.getArtifactId(), requestURI, remoteAddr, LocalDateTime.now());
        statsClient.saveHit(hit).block();
        log.info("Stats saved successfully for request URI: {}", requestURI);
    }

    @Transactional
    private void updateEventViews(Event event, Long eventId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_PATTERN);

        String startDateTime = LocalDateTime.now().minusYears(30).format(formatter);
        String endDateTime = LocalDateTime.now().format(formatter);

        List<ViewStats> stats = statsClient.getStats(
                startDateTime,
                endDateTime,
                List.of("/events/" + eventId),
                true
        ).block();

        if (stats != null) {
            if (stats.isEmpty()) {
                event.setViews(1L);
            } else {
                event.setViews(stats.get(0).getHits());
            }
        }

        eventRepository.save(event);
        log.info("Event views updated successfully for Event ID: {}", eventId);
    }

    @Transactional
    private Event prepareEventData(EventDto eventDto, Long userId) {
        Event event = EventMapper.toEntity(eventDto);

        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new ObjectNotFoundException("Category not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        Location location = LocationMapper.toEntity(eventDto.getLocation());
        Location savedLocation = locationRepository.save(location);

        event.setCategory(category);
        event.setInitiator(user);
        event.setLocation(savedLocation);

        return event;
    }

    @Transactional
    private void mapEventUpdateRequestToEvent(EventUpdateRequest request, Event event) {
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            event.setCategory(categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new ObjectNotFoundException("Category not found")));
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getLocation() != null) {
            Location location = LocationMapper.toEntity(request.getLocation());
            Location savedLocation = locationRepository.save(location);
            event.setLocation(savedLocation);
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
    }

    @Override
    @Transactional
    public EventOutputDto create(EventDto eventDto, Long userId) {
        validateEventDto(eventDto);
        Event event = prepareEventData(eventDto, userId);
        Event savedEvent = eventRepository.save(event);
        log.info("Event successfully created: Event ID - {}", savedEvent.getId());
        return EventMapper.toOutputDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventOutputDto getByCreator(Long eventId, Long userId) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId);
        log.info("Returning event by ID: Event ID - {}", eventId);
        return EventMapper.toOutputDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAlLByCreator(Long userId, Integer from, Integer size) {
        Pageable pageable = new PageCalculation(from, size);
        List<EventShortDto> events = eventRepository.findByInitiator_Id(userId, pageable).stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
        log.info("Returning all user events for User ID - {}", userId);
        return events;
    }

    @Override
    @Transactional
    public EventOutputDto updateByCreator(EventUpdateRequest eventUpdateRequest, Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ObjectNotFoundException("You do not have access to this event");
        }

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ObjectAlreadyExistsException("Only pending or canceled events can be changed");
        }

        mapEventUpdateRequestToEvent(eventUpdateRequest, event);

        StateAction stateAction = eventUpdateRequest.getStateAction();
        if (stateAction != null) {
            switch (stateAction) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
            }
        }

        Event updatedEvent = eventRepository.save(event);
        log.info("Event updated successfully: Event ID - {}", updatedEvent.getId());
        return EventMapper.toOutputDto(updatedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventOutputDto> search(List<Long> users, List<State> states, List<Long> categories,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable pageable = new PageCalculation(from, size);
        Specification<Event> searchSpec = EventSpecifications.search(users, categories, states, rangeStart, rangeEnd);
        List<EventOutputDto> events = eventRepository.findAll(searchSpec, pageable).stream()
                .map(EventMapper::toOutputDto)
                .collect(Collectors.toList());
        log.info("Returning {} found events", events.size());
        return events;
    }

    @Override
    @Transactional
    public EventOutputDto updateByAdmin(EventUpdateRequest eventUpdateRequest, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event not found"));

        mapEventUpdateRequestToEvent(eventUpdateRequest, event);

        StateAction stateAction = eventUpdateRequest.getStateAction();
        if (stateAction != null) {
            switch (stateAction) {
                case PUBLISH_EVENT:
                    if (!event.getState().equals(State.PENDING)) {
                        throw new ObjectAlreadyExistsException("Only pending events can be changed");
                    }
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    if (event.getState().equals(State.PUBLISHED)) {
                        throw new ObjectAlreadyExistsException("Only unpublished events can be changed");
                    }
                    event.setState(State.REJECTED);
                    break;
            }
        }

        Event updatedEvent = eventRepository.save(event);
        log.info("Event updated successfully: Event ID - {}", updatedEvent.getId());
        return EventMapper.toOutputDto(updatedEvent);
    }

    @Override
    @Transactional
    public List<EventShortDto> searchWithFilters(String text, List<Long> categories, Boolean paid,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                 Boolean onlyAvailable, SortType sort, Integer from, Integer size,
                                                 HttpServletRequest request) {
        LocalDateTime start;

        start = Objects.requireNonNullElseGet(rangeStart, LocalDateTime::now);

        if (rangeEnd != null && rangeEnd.isBefore(start)) {
            throw new IllegalArgumentException("Start cannot be after the end");
        }

        Pageable pageable = new PageCalculation(from, size);

        if (sort != null) {
            switch (sort) {
                case EVENT_DATE:
                    pageable = new PageCalculation(from, size, Sort.by(Sort.Direction.DESC, "eventDate"));
                    break;
                case VIEWS:
                    pageable = new PageCalculation(from, size, Sort.by(Sort.Direction.DESC, "views"));
                    break;
            }
        }

        Specification<Event> spec = EventSpecifications.withFilters(text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, onlyAvailable);

        List<EventShortDto> events = eventRepository.findAll(spec, pageable).stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());

        saveStats(request.getRequestURI(), request.getRemoteAddr());
        log.info("Returning {} found events", events.size());
        return events;
    }

    @Override
    @Transactional
    public EventOutputDto get(Long eventId, HttpServletRequest request) {
        saveStats(request.getRequestURI(), request.getRemoteAddr());

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event not found"));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ObjectNotFoundException("Event not found");
        }

        updateEventViews(event, eventId);
        log.info("Returning found event for Event ID: {}", eventId);
        return EventMapper.toOutputDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ObjectNotFoundException("You do not have access to this event");
        }

        List<ParticipationRequestDto> requests = requestRepository.findByEvent_Id(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        log.info("Returning {} found requests for Event ID - {}", requests.size(), eventId);
        return requests;
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event not found"));

        List<Request> requests = requestRepository.findByIdInAndEvent_Id(eventRequestStatusUpdateRequest.getRequestIds(), eventId);
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            throw new IllegalArgumentException("For this event, request confirmation is not required");
        }

        long limitBalance = event.getParticipantLimit() - event.getConfirmedRequests();
        if (event.getParticipantLimit() != 0 && limitBalance <= 0) {
            throw new ObjectAlreadyExistsException("The participation limit has been exceeded");
        }

        List<Request> requestsToSave = new ArrayList<>();
        if (eventRequestStatusUpdateRequest.getStatus().equals(State.REJECTED)) {
            for (Request request : requests) {
                request.setStatus(State.REJECTED);
                requestsToSave.add(request);
                rejected.add(request);
            }
        }

        if (eventRequestStatusUpdateRequest.getStatus().equals(State.CONFIRMED)) {
            for (Request request : requests) {
                if (limitBalance > 0) {
                    request.setStatus(State.CONFIRMED);
                    requestsToSave.add(request);
                    confirmed.add(request);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    limitBalance--;
                } else {
                    request.setStatus(State.REJECTED);
                    requestsToSave.add(request);
                    confirmed.add(request);
                }
            }
        }

        eventRepository.save(event);
        requestRepository.saveAll(requestsToSave);

        List<ParticipationRequestDto> rejectedParticipationRequestDto = rejected.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        List<ParticipationRequestDto> confirmedParticipationRequestDto = confirmed.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());

        EventRequestStatusUpdateResult result = EventRequestStatusUpdateResult.builder()
                .rejectedRequests(rejectedParticipationRequestDto)
                .confirmedRequests(confirmedParticipationRequestDto)
                .build();
        log.info("The update of requests was successfully completed for Event ID - {}", eventId);
        return result;
    }
}