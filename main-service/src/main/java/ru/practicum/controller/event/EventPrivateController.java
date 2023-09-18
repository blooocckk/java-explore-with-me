package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventOutputDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.EventUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventOutputDto> create(@Valid @RequestBody EventDto eventDto,
                                                 @PathVariable Long userId) {
        log.info("Received a request to create an event by user ID: {} with data: {}", userId, eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(eventDto, userId));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventOutputDto> getByCreator(@PathVariable Long userId,
                                                       @PathVariable Long eventId) {
        log.info("Received a request to get event by ID: {}", eventId);
        return ResponseEntity.ok(eventService.getByCreator(eventId, userId));
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getAllByCreator(@PathVariable Long userId,
                                                               @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                               @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Received a request to get all events by creator with ID: {}, from: {}, size: {}", userId, from, size);
        return ResponseEntity.ok(eventService.getAlLByCreator(userId, from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventOutputDto> updateByCreator(@Valid @RequestBody EventUpdateRequest eventUpdateRequest,
                                                          @PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        log.info("Received a request to update event by ID: {}", eventId);
        return ResponseEntity.ok(eventService.updateByCreator(eventUpdateRequest, eventId, userId));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable Long userId,
                                                                     @PathVariable Long eventId) {
        log.info("Received a request to get participation requests for event with ID: {}", eventId);
        return ResponseEntity.ok(eventService.getRequests(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequestStatus(@RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                                              @PathVariable Long userId,
                                                                              @PathVariable Long eventId) {
        log.info("Received a request to update request status for event with ID: {}", eventId);
        return ResponseEntity.ok(eventService.updateRequestStatus(userId, eventId, eventRequestStatusUpdateRequest));
    }
}
