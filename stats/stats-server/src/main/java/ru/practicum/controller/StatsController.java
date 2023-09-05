package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.EndpointHitDto;
import ru.practicum.OutputHitDto;
import ru.practicum.ViewStats;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<OutputHitDto> hitEndpoint(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Received a request to save statistics");
        return ResponseEntity.status(HttpStatus.CREATED).body(statsService.saveHit(endpointHitDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStats>> getStats(@RequestParam @DateTimeFormat(pattern = Constants.DATE_TIME_PATTERN) LocalDateTime start,
                                                    @RequestParam @DateTimeFormat(pattern = Constants.DATE_TIME_PATTERN) LocalDateTime end,
                                                    @RequestParam(required = false) List<String> uris,
                                                    @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Received a request to retrieve all statistics");
        return ResponseEntity.status(HttpStatus.OK).body(statsService.getStats(start, end, uris, unique));
    }
}