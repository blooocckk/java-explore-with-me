package ru.practicum.mapper.event;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventOutputDto;
import ru.practicum.dto.event.EventShortDto;
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
        return EventOutputDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .category(CategoryMapper.toOutputDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .location(LocationMapper.toDto(event.getLocation()))
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .views(event.getViews())
                .title(event.getTitle())
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
