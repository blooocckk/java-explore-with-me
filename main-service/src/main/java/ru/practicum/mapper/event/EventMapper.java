package ru.practicum.mapper.event;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventOutputDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.output.EventDateTimeDto;
import ru.practicum.dto.event.output.EventDetailsDto;
import ru.practicum.dto.event.output.EventStatsDto;
import ru.practicum.mapper.category.CategoryMapper;
import ru.practicum.mapper.location.LocationMapper;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.model.event.Event;

@UtilityClass
public class EventMapper {
    public EventDto toDto(Event event) {
        return EventDto.builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory().getId())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .title(event.getTitle())
                .build();
    }

    public EventOutputDto toOutputDto(Event event) {
        EventDetailsDto eventDetailsDto = EventDetailsDto.builder()
                .description(event.getDescription())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toOutputDto(event.getCategory()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .location(LocationMapper.toDto(event.getLocation()))
                .state(event.getState())
                .title(event.getTitle())
                .publishedOn(event.getPublishedOn())
                .build();

        EventDateTimeDto eventDateTimeDto = EventDateTimeDto.builder()
                .createdOn(event.getCreatedOn())
                .eventDate(event.getEventDate())
                .build();

        EventStatsDto eventStatsDto = EventStatsDto.builder()
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .build();

        return EventOutputDto.builder()
                .id(event.getId())
                .eventDetails(eventDetailsDto)
                .eventDateTime(eventDateTimeDto)
                .eventStats(eventStatsDto)
                .build();
    }


    public EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .category(CategoryMapper.toOutputDto(event.getCategory()))
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toEntity(EventDto eventDto) {
        return Event.builder()
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .eventDate(eventDto.getEventDate())
                .location(LocationMapper.toEntity(eventDto.getLocation()))
                .paid(eventDto.getPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration())
                .title(eventDto.getTitle())
                .build();
    }
}
