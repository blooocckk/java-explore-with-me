package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.event.output.EventDateTimeDto;
import ru.practicum.dto.event.output.EventDetailsDto;
import ru.practicum.dto.event.output.EventStatsDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventOutputDto {
    private Long id;

    @JsonUnwrapped
    private EventDetailsDto eventDetails;

    @JsonUnwrapped
    private EventDateTimeDto eventDateTime;

    @JsonUnwrapped
    private EventStatsDto eventStats;
}

