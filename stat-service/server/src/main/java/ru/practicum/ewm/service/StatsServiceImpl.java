package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    @Override
    public void saveHit(EndpointHitDto endpointHitDto) {
        log.info("Сохранение запроса POST в сервисе с endPointDto {}", endpointHitDto.toString());
        repository.saveHit(endpointHitDto);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Получение информации по запросу GET в сервисе c start {}, end {}, uris {}, unique {}", start, end,
                uris, unique);
        if (start == null || end == null) {
            log.error("Отсутствует дата начала или окончания поиска. Start = {}, end = {}", start, end);
            throw new BadRequestException("Отсутствует дата начала или окончания поиска");
        }
        if (start.isAfter(end)) {
            log.error("Некорректные даты начала и окончания поиска");
            throw new BadRequestException("Дата начала не может быть после даты окончания поиска");
        }
        if (unique) {
            log.info("Запрос данных с уникальными значениями");
            return repository.findUniqueStats(start, end, uris);
        }
        log.info("Запрос данных со всеми значениями");
        return repository.findStats(start, end, uris);
    }
}
