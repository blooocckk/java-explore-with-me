package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ViewStats;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT NEW ru.practicum.ViewStats(app, uri, COUNT(ip) AS hits) " +
            "FROM ru.practicum.model.EndpointHit " +
            "WHERE timestamp BETWEEN :start AND :end " +
            "AND uri IN :uris " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC")
    List<ViewStats> findStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query("SELECT NEW ru.practicum.ViewStats(app, uri, COUNT(distinct ip) AS hits) " +
            "FROM ru.practicum.model.EndpointHit " +
            "WHERE timestamp BETWEEN :start AND :end " +
            "AND uri IN :uris " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC")
    List<ViewStats> findUniqueStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query("SELECT NEW ru.practicum.ViewStats(app, uri, COUNT(ip) AS hits) " +
            "FROM ru.practicum.model.EndpointHit " +
            "WHERE timestamp BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC")
    List<ViewStats> findStatsForAll(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT NEW ru.practicum.ViewStats(app, uri, COUNT(distinct ip) AS hits) " +
            "FROM ru.practicum.model.EndpointHit " +
            "WHERE timestamp BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC")
    List<ViewStats> findUniqueStatsForAll(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}