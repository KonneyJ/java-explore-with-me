package ru.practicum.ewm.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.ewm.mapper.ViewStatsMapper;
import ru.practicum.ewm.model.ViewStats;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatsRepositoryImpl implements StatsRepository {
    private final JdbcTemplate jdbc;
    private final ViewStatsMapper mapper;

    @Override
    public void saveHit(EndpointHitDto dto) {
        log.info("Сохранение запроса POST hit в репозитории");
        jdbc.update("INSERT INTO stats (app, uri, ip, created) VALUES ( ?, ?, ?, ?)", dto.getApp(), dto.getUri(),
                dto.getIp(), Timestamp.valueOf(dto.getTimestamp()));
    }

    @Override
    public List<ViewStatsDto> findUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        log.info("Получение информации о статистике с уникальными значениями в репозитории");
        String query = "SELECT app, uri, COUNT(DISTINCT ip) AS hits FROM stats WHERE (created BETWEEN ? AND ?) " +
                "AND uri IN (";
        StringBuilder string = new StringBuilder("'");
        string.append(String.join("', '", uris)).append("') ").toString();
        query += string + "GROUP BY app, uri ORDER BY hits DESC";
        return jdbc.query(query, mapper, start, end);
    }

    @Override
    public List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        log.info("Получение информации о статистике со всеми значениями в репозитории");
        String query = "SELECT app, uri, COUNT(ip) AS hits FROM stats WHERE (created BETWEEN ? AND ?) " +
                "AND uri IN (";
        StringBuilder string = new StringBuilder("'");
        string.append(String.join("', '", uris)).append("') ").toString();
        query += string + "GROUP BY app, uri ORDER BY hits DESC";
        return jdbc.query(query, mapper, start, end);
    }
}
