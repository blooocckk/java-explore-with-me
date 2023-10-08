package ru.practicum.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentOutputDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentOutputDto> create(@Valid @RequestBody CommentDto commentDto,
                                                   @PathVariable Long userId,
                                                   @RequestParam(value = "eventId") Long eventId) {
        log.info("Received a request to create a comment for user ID: {} and event ID: {}", userId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(commentDto, eventId, userId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> delete(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("Received a request to delete a comment with ID: {} for user ID: {}", commentId, userId);
        commentService.delete(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentOutputDto> update(@Valid @RequestBody CommentDto commentDto,
                                                   @PathVariable Long userId,
                                                   @PathVariable Long commentId) {
        log.info("Received a request to update a comment with ID: {} for user ID: {}", commentId, userId);
        return ResponseEntity.ok(commentService.update(commentDto, commentId, userId));
    }
}