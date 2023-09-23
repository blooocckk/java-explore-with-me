package ru.practicum.mapper.location;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.event.LocationDto;
import ru.practicum.model.event.Location;

@UtilityClass
public class LocationMapper {

    public Location toEntity(LocationDto dto) {
        return Location.builder()
                .lon(dto.getLon())
                .lat(dto.getLat())
                .build();
    }

    public LocationDto toDto(Location location) {
        return LocationDto.builder()
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }
}
