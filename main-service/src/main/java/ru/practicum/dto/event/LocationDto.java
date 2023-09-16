package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    @NotBlank(message = "Lat cannot be empty")
    private Double lat;

    @NotBlank(message = "Lon cannot be empty")
    private Double lon;
}
