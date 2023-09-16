package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private List<Long> events;
    private Boolean pinned;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    private String title;
}
