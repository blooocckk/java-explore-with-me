package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.EndpointHitDto;
import ru.practicum.OutputHitDto;
import ru.practicum.model.EndpointHit;

@UtilityClass
public class HitMapper {
    public EndpointHitDto toDto(EndpointHit hit) {
        return EndpointHitDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }

    public OutputHitDto toOutputDto(EndpointHit hit) {
        return OutputHitDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }

    public EndpointHit toEntity(EndpointHitDto hitInputDto) {
        return EndpointHit.builder()
                .app(hitInputDto.getApp())
                .uri(hitInputDto.getUri())
                .ip(hitInputDto.getIp())
                .timestamp(hitInputDto.getTimestamp())
                .build();
    }
}