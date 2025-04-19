package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.enums.EventState;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {

    Collection<EventFullDto> getAllEventsByAdmin(List<Integer> users, List<EventState> states, List<Integer> categories,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest);

    Collection<EventShortDto> getAllEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size);

    EventFullDto getEventById(int eventId);

    Collection<EventShortDto> getAllEventsByUser(int userId, int from, int size);

    EventFullDto createEvent(int userId, NewEventDto newEventDto);

    EventFullDto getEventByIdByUser(int userId, int eventId);

    EventFullDto updateEventByUser(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest);

    Collection<ParticipationRequestDto> getAllEventsRequestsByUser(int userId, int eventId);

    EventRequestStatusUpdateResult updateEventRequestByUser(int userId, int eventId, EventRequestStatusUpdateRequest
            eventRequestStatusUpdateRequest);
}
