package ru.practicum.ewm.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.practicum.ViewStatsDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ViewStatsMapper implements RowMapper<ViewStatsDto> {

    @Override
    public ViewStatsDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        ViewStatsDto stat = new ViewStatsDto();
        stat.setApp(resultSet.getString("app"));
        stat.setUri(resultSet.getString("uri"));
        stat.setHits(resultSet.getInt("hits"));
        return stat;
    }
}
