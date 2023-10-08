package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentOutputDto;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.mapper.comment.CommentMapper;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;
import ru.practicum.model.pagination.PageCalculation;
import ru.practicum.model.user.User;
import ru.practicum.repository.comment.CommentRepository;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentOutputDto create(CommentDto commentDto, Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new IllegalArgumentException("Commenting on unpublished events is not allowed");
        }

        Comment comment = CommentMapper.toEntity(commentDto);
        comment.setUser(user);
        comment.setEvent(event);

        Comment savedComment = commentRepository.save(comment);
        log.info("Comment created successfully: Comment ID - {}", savedComment.getId());
        return CommentMapper.toOutputDto(savedComment);
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ObjectNotFoundException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new ObjectNotFoundException("You do not have access to this comment");
        }

        commentRepository.deleteById(commentId);
        log.info("Comment deleted successfully: Comment ID - {}", commentId);
    }

    @Override
    @Transactional
    public CommentOutputDto update(CommentDto commentDto, Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ObjectNotFoundException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new ObjectNotFoundException("You do not have access to this comment");
        }

        comment.setContent(commentDto.getContent());
        Comment updatedComment = commentRepository.save(comment);
        log.info("Comment updated successfully: Comment ID - {}", updatedComment.getId());
        return CommentMapper.toOutputDto(updatedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentOutputDto getById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ObjectNotFoundException("Comment not found"));
        log.info("Returning comment by ID: Comment ID - {}", commentId);
        return CommentMapper.toOutputDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentOutputDto> getAllByEvent(Long eventId, Integer from, Integer size) {
        Pageable pageable = new PageCalculation(from, size);
        List<CommentOutputDto> events = commentRepository.findByEvent_Id(eventId, pageable).stream()
                .map(CommentMapper::toOutputDto)
                .collect(Collectors.toList());
        log.info("Returning all comments for Event ID - {}", eventId);
        return events;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentOutputDto> getAllByCreator(Long userId, Integer from, Integer size) {
        Pageable pageable = new PageCalculation(from, size);
        List<CommentOutputDto> events = commentRepository.findByUser_Id(userId, pageable).stream()
                .map(CommentMapper::toOutputDto)
                .collect(Collectors.toList());
        log.info("Returning all comments for User ID - {}", userId);
        return events;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentOutputDto> search(String text, Integer from, Integer size) {
        Pageable pageable = new PageCalculation(from, size);
        List<CommentOutputDto> events = commentRepository.findByContentContaining(text, pageable).stream()
                .map(CommentMapper::toOutputDto)
                .collect(Collectors.toList());
        log.info("Returning {} found comments", events.size());
        return events;
    }
}
