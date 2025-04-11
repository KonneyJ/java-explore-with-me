package ru.practicum.ewm.repository;

import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository {

    void saveHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> findUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris);

}
