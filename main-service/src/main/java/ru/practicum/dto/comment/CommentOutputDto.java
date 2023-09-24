package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Constants;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentOutputDto {
    private Long id;
    private String content;
    private Long event;
    private UserShortDto user;

    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime createdOn;
}
