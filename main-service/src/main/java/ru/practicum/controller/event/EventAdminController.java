package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.dto.event.EventOutputDto;
import ru.practicum.dto.event.EventUpdateRequest;
import ru.practicum.model.event.State;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventOutputDto> updateByCreator(@Valid @RequestBody EventUpdateRequest eventUpdateRequest,
                                                          @PathVariable Long eventId) {
        log.info("Received a request to update event by admin for event ID: {}", eventId);
        return ResponseEntity.ok(eventService.updateByAdmin(eventUpdateRequest, eventId));
    }

    @GetMapping
    public ResponseEntity<List<EventOutputDto>> search(@RequestParam(required = false) List<Long> users,
                                                       @RequestParam(required = false) List<State> states,
                                                       @RequestParam(required = false) List<Long> categories,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                       @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Received a search request for events with parameters: users={}, states={}, categories={}, " +
                        "rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return ResponseEntity.ok(eventService.search(users, states, categories, rangeStart, rangeEnd, from, size));
    }
}
