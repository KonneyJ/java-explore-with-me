package ru.practicum.ewm.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
 @Validated
public class EventPrivateController {
    private final EventService eventService;

    @GetMapping
    public Collection<EventShortDto> getAllEventsByUser(@PathVariable("userId") int userId,
                                                        @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                        @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("PRIVATE GET /users/{userId}/events запрос with userId {}, from {}, size {}", userId, from, size);
        return eventService.getAllEventsByUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable("userId") int userId,
                                    @Valid @RequestBody NewEventDto newEventDto) {
        log.info("PRIVATE POST /users/{userId}/events запрос with userId {}, newEventDto {}", userId, newEventDto);
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByIdByUser(@PathVariable("userId") int userId,
                                           @PathVariable("eventId") int eventId) {
        log.info("PRIVATE GET /users/{userId}/events/{eventId} запрос with userId {}, eventId {}", userId, eventId);
        return eventService.getEventByIdByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable("userId") int userId,
                                          @PathVariable("eventId") int eventId,
                                          @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("PRIVATE PATCH /users/{userId}/events/{eventId} запрос with userId {}, eventId {}, updateRequest {}",
                userId, eventId, updateEventUserRequest);
        return eventService.updateEventByUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public Collection<ParticipationRequestDto> getAllEventsRequestsByUser(@PathVariable("userId") int userId,
                                                                          @PathVariable("eventId") int eventId) {
        log.info("PRIVATE GET /users/{userId}/events/{eventId}/requests запрос with userId {}, eventId {}",
                userId, eventId);
        return eventService.getAllEventsRequestsByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestByUser(@PathVariable("userId") int userId,
                                                                   @PathVariable("eventId") int eventId,
                                                                   @RequestBody EventRequestStatusUpdateRequest
                                                                           eventRequestStatusUpdateRequest) {
        log.info("PRIVATE PATCH /users/{userId}/events/{eventId}/requests запрос with userId {}, eventId {}, " +
                "updateRequest {}", userId, eventId, eventRequestStatusUpdateRequest);
        return eventService.updateEventRequestByUser(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
