package ru.practicum.mapper.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentOutputDto;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.model.comment.Comment;

@UtilityClass
public class CommentMapper {
    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .content(comment.getContent())
                .build();
    }

    public CommentOutputDto toOutputDto(Comment comment) {
        return CommentOutputDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(UserMapper.toShortDto(comment.getUser()))
                .event(comment.getEvent().getId())
                .createdOn(comment.getCreatedOn())
                .build();
    }

    public Comment toEntity(CommentDto commentDto) {
        return Comment.builder()
                .content(commentDto.getContent())
                .build();
    }
}
