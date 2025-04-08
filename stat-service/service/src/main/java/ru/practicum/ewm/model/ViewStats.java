package ru.practicum.ewm.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStats {
    private String app;
    private String uri;
    private int hits;
}
