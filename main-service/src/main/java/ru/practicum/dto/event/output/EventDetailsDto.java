package ru.practicum.dto.event.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Constants;
import ru.practicum.dto.category.CategoryOutputDto;
import ru.practicum.dto.event.LocationDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.event.State;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDetailsDto {
    private String description;
    private String annotation;
    private CategoryOutputDto category;
    private boolean requestModeration;
    private boolean paid;
    private Long participantLimit;
    private UserShortDto initiator;
    private LocationDto location;
    private State state;
    private String title;

    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime publishedOn;
}