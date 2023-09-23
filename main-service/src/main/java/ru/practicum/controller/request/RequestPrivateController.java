package ru.practicum.controller.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.request.RequestService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestPrivateController {
    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> create(@PathVariable Long userId,
                                                          @RequestParam(value = "eventId") Long eventId) {
        log.info("Received a request to create a participation request for user ID: {} and event ID: {}", userId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.create(userId, eventId));
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getAll(@PathVariable Long userId) {
        log.info("Received a request to get all participation requests for user ID: {}", userId);
        return ResponseEntity.ok(requestService.getAll(userId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> update(@PathVariable Long userId,
                                                          @PathVariable Long requestId) {
        log.info("Received a request to cancel participation request with ID: {} for user ID: {}", requestId, userId);
        return ResponseEntity.ok(requestService.update(userId, requestId));
    }
}
