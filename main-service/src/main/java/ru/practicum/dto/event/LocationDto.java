package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    @NotNull(message = "Lat cannot be empty")
    private Double lat;

    @NotNull(message = "Lon cannot be empty")
    private Double lon;
}
