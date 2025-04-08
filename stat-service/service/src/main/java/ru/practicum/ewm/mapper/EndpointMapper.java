package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.EndpointHitDto;
import ru.practicum.ewm.model.EndpointHit;

@Component
public class EndpointMapper {
    public static EndpointHit toEntity(EndpointHitDto dto) {
        return EndpointHit.builder()
                .id(dto.getId())
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }
}
