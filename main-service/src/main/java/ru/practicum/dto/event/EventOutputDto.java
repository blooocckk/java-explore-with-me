package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Constants;
import ru.practicum.dto.category.CategoryOutputDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.event.State;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventOutputDto {
    private Long id;
    private String annotation;
    private CategoryOutputDto category;
    private Integer confirmedRequests;

    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime createdOn;
    private String description;

    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private LocationDto location;
    private boolean paid;
    private Long participantLimit;

    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private State state;
    private String title;
    private Long views;
}
