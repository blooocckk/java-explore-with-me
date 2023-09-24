package ru.practicum.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentOutputDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
@Validated
public class CommentPublicController {
    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentOutputDto> getById(@PathVariable Long commentId) {
        log.info("Received a request to get comment with ID: {}", commentId);
        return ResponseEntity.ok(commentService.getById(commentId));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<CommentOutputDto>> getAllByEvent(@PathVariable Long eventId,
                                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Received a request to get all comments for event ID: {}", eventId);
        return ResponseEntity.ok(commentService.getAllByEvent(eventId, from, size));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentOutputDto>> getAllByUser(@PathVariable Long userId,
                                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                               @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Received a request to get all comments for user ID: {}", userId);
        return ResponseEntity.ok(commentService.getAllByCreator(userId, from, size));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CommentOutputDto>> search(@RequestParam String text,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Received a request to search comments with text: {}", text);
        return ResponseEntity.ok(commentService.search(text, from, size));
    }
}
