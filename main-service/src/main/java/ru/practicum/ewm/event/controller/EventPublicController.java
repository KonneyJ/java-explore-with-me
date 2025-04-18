package ru.practicum.ewm.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.service.EventService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventPublicController {
    private final EventService eventService;
    //private final StatsClient statsClient;

    @GetMapping
    public Collection<EventShortDto> getAllEvents(@RequestParam(required = false) String text,
                                                  @RequestParam(required = false) List<Integer> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                  LocalDateTime rangeStart,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                  LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam(required = false) String sort,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size,
                                                  HttpServletRequest request) {
        log.info("PUBLIC GET /events запрос with text {}, categories {}, paid {}, rangeStart {}, rangeEnd {}, " +
                        "onlyAvailable {}, sort {}, from {}, size {}", text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        postHit(request);
        return eventService.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable("id") int eventId, HttpServletRequest request) {
        log.info("PUBLIC GET /events/id запрос with id {}", eventId);
        postHit(request);
        return eventService.getEventById(eventId);
    }

    private void postHit(HttpServletRequest request) {
        log.info("Передача информации о запросе в сервис статистики");
        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();
        EndpointHitDto hitDto = EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri(path)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();
        //statsClient.postHit(hitDto);
        log.info("Информация успешно отправлена");
    }
}
