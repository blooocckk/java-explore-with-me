package ru.practicum;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {
    String app;
    String uri;
    Long hits;
}