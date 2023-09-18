package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Constants;
import ru.practicum.exception.validation.ValidEventDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    @NotBlank(message = "Annotation cannot be empty")
    @Size(min = 20, max = 2000, message = "Annotation must be between 20 and 2000 characters")
    private String annotation;

    @NotNull(message = "Category cannot be empty")
    private Long category;

    @NotBlank(message = "Description cannot be empty")
    @Size(min = 20, max = 7000, message = "Description must be between 20 and 7000 characters")
    private String description;

    @Valid
    @NotNull(message = "Location cannot be empty")
    private LocationDto location;

    @NotNull(message = "Event date cannot be empty")
    @ValidEventDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 3, max = 120, message = "Title must be between 3 and 120 characters")
    private String title;
}
