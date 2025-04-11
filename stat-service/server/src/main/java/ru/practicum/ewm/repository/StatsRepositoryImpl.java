package ru.practicum.ewm.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.ewm.exception.InternalServerException;
import ru.practicum.ewm.mapper.ViewStatsMapper;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StatsRepositoryImpl implements StatsRepository {
    private final JdbcTemplate jdbc;
    private final ViewStatsMapper mapper;

    @Override
    public void saveHit(EndpointHitDto dto) {
        log.info("Сохранение запроса POST hit в репозитории");
        String query = "INSERT INTO statistics (app, uri, ip, created) VALUES ( ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, new String[]{"id"});
            ps.setString(1, dto.getApp());
            ps.setString(2, dto.getUri());
            ps.setString(3, dto.getIp());
            ps.setTimestamp(4, Timestamp.valueOf(dto.getTimestamp()));
            return ps;
        }, keyHolder);

        Integer id = keyHolder.getKeyAs(Integer.class);

        // Возвращаем id нового сохранения
        if (id != null) {
            dto.setId(id);
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        log.info("Сохранение успешно выполнено c id {}", id);
    }

    @Override
    public List<ViewStatsDto> findUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        log.info("Получение информации о статистике со всеми значениями в репозитории");
        String query = "SELECT app, uri, COUNT(DISTINCT ip) AS hits FROM statistics WHERE (created BETWEEN ? AND ?) ";
        if (uris != null) {
            StringBuilder string = new StringBuilder("AND uri IN ('");
            string.append(String.join("', '", uris)).append("') ").toString();
            query += string;
        }
        query += "GROUP BY app, uri ORDER BY hits DESC";
        log.info("Сформировано query для запроса {}", query);
        return jdbc.query(query, mapper, start, end);
    }

    @Override
    public List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        log.info("Получение информации о статистике со всеми значениями в репозитории");
        String query = "SELECT app, uri, COUNT(ip) AS hits FROM statistics WHERE (created BETWEEN ? AND ?) ";
        if (uris != null) {
            StringBuilder string = new StringBuilder("AND uri IN ('");
            string.append(String.join("', '", uris)).append("') ").toString();
            query += string;
        }
        query += "GROUP BY app, uri ORDER BY hits DESC";
        log.info("Сформировано query для запроса {}", query);
        return jdbc.query(query, mapper, start, end);
    }
}
