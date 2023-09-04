package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStats;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Transactional
    @Override
    public EndpointHitDto saveHit(EndpointHitDto endpointHitDto) {
        EndpointHit hit = HitMapper.toEntity(endpointHitDto);
        EndpointHitDto savedHit = HitMapper.toDto(statsRepository.save(hit));
        log.info("Statistics saved");
        return savedHit;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris == null) {
            if (unique) {
                log.info("Returning statistics for unique visits for all URLs");
                return statsRepository.findUniqueStatsForAll(start, end);
            } else {
                log.info("Returning statistics for all URLs");
                return statsRepository.findStatsForAll(start, end);
            }
        } else {
            if (unique) {
                log.info("Returning statistics for unique visits for requested URLs");
                return statsRepository.findUniqueStats(start, end, uris);
            } else {
                log.info("Returning statistics for requested URLs");
                return statsRepository.findStats(start, end, uris);
            }
        }
    }
}