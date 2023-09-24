package ru.practicum.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    @NotBlank(message = "Content cannot be empty")
    @Size(min = 1, max = 1500, message = "Content must be between 1 and 1500 characters")
    private String content;
}
