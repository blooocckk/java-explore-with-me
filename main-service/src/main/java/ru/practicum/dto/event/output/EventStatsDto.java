package ru.practicum.dto.event.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventStatsDto {
    private Integer confirmedRequests;
    private Long views;
}