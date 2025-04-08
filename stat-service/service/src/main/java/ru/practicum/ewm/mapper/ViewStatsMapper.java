package ru.practicum.ewm.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.practicum.ViewStatsDto;
import ru.practicum.ewm.model.ViewStats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ViewStatsMapper implements RowMapper<ViewStatsDto> {
    /*public static ViewStatsDto toDto(ViewStats stat) {
        return ViewStatsDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits(stat.getHits())
                .build();
    }

    public static List<ViewStatsDto> toDtoList(List<ViewStats> stats) {
        return stats.stream().map(ViewStatsMapper::toDto).collect(Collectors.toList());
    }*/

    @Override
    public ViewStatsDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        ViewStatsDto stat = new ViewStatsDto();
        stat.setApp(resultSet.getString("app"));
        stat.setUri(resultSet.getString("uri"));
        stat.setHits(resultSet.getInt("hits"));
        return stat;
    }
}
