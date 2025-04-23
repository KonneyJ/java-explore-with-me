package ru.practicum;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ViewStatsDto {
    private String app;
    private String uri;
    private int hits;
}
