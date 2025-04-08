package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository { //extends JpaRepository<EndpointHit, Integer> {

    void saveHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> findUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    /*@Query("SELECT new ru.practicum.ewm.model.ViewStats(eph.app, eph.uri, COUNT(DISTINCT eph.ip) AS hits) " +
            "FROM EndpointHit eph " +
            "WHERE eph.timestamp BETWEEN :start AND :end " +
            "AND (eph.uri IN (:uris) OR :uris IS NULL) " +
            "GROUP BY eph.app, eph.uri " +
            "ORDER BY hits DESC")
    List<ViewStats> findUniqueStats(@Param("start")LocalDateTime start, @Param("end") LocalDateTime end,
                                    @Param("uris") List<String> uris);

    @Query("SELECT eph.app, eph.uri, COUNT(DISTINCT eph.ip) AS hits " +
            "FROM EndpointHit eph " +
            "WHERE eph.timestamp BETWEEN :start AND :end " +
            "AND (eph.uri IN (:uris) OR :uris IS NULL) " +
            "GROUP BY eph.app, eph.uri " +
            "ORDER BY hits DESC")
    List<Object[]> findUniqueStats(@Param("start")LocalDateTime start, @Param("end") LocalDateTime end,
                                    @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.ewm.model.ViewStats(eph.app, eph.uri, COUNT(eph.ip) AS hits) " +
            "FROM EndpointHit eph " +
            "WHERE eph.timestamp BETWEEN :start AND :end " +
            "AND (eph.uri IN (:uris) OR :uris IS NULL) " +
            "GROUP BY eph.app, eph.uri " +
            "ORDER BY hits DESC")
    List<ViewStats> findStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                              @Param("uris") List<String> uris);*/

}
