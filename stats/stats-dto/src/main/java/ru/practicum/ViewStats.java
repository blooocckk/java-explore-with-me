package ru.practicum;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}