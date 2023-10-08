package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.dto.event.EventOutputDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.model.event.SortType;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventPublicController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(@RequestParam(required = false) String text,
                                                         @RequestParam(required = false) List<Long> categories,
                                                         @RequestParam(required = false) Boolean paid,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                         @RequestParam(required = false) SortType sort,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                         @RequestParam(defaultValue = "10") @Positive Integer size,
                                                         HttpServletRequest request) {
        log.info("Received a request to get events with parameters: text={}, categories={}, paid={}, rangeStart={}, " +
                        "rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return ResponseEntity.ok(eventService.searchWithFilters(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventOutputDto> getById(@PathVariable Long eventId,
                                                             HttpServletRequest request) {
        log.info("Received a request to get full public event with ID: {}", eventId);
        return ResponseEntity.ok(eventService.get(eventId, request));
    }
}
