package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentOutputDto;

import java.util.List;

public interface CommentService {
    CommentOutputDto create(CommentDto commentDto, Long eventId, Long userId);

    void delete(Long commentId, Long userId);

    CommentOutputDto update(CommentDto commentDto, Long commentId, Long userId);

    CommentOutputDto getById(Long commentId);

    List<CommentOutputDto> getAllByEvent(Long eventId, Integer from, Integer size);

    List<CommentOutputDto> getAllByCreator(Long userId, Integer from, Integer size);

    List<CommentOutputDto> search(String text, Integer from, Integer size);
}
