package ru.practicum.ewm.event.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.enums.EventState;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public Collection<EventFullDto> getAllEventsByAdmin(List<Integer> users, List<EventState> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        return List.of();
    }

    @Override
    public EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        return null;
    }

    @Override
    public Collection<EventShortDto> getAllEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
        return List.of();
    }

    @Override
    public EventFullDto getEventById(int eventId) {
        return null;
    }

    @Override
    public Collection<EventShortDto> getAllEventsByUser(int userId, int from, int size) {
        return List.of();
    }

    @Override
    public EventFullDto createEvent(int userId, NewEventDto newEventDto) {
        return null;
    }

    @Override
    public EventFullDto getEventByIdByUser(int userId, int eventId) {
        return null;
    }

    @Override
    public EventFullDto updateEventByUser(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest) {
        return null;
    }

    @Override
    public Collection<ParticipationRequestDto> getAllEventsRequestsByUser(int userId, int eventId) {
        return List.of();
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequestByUser(int userId, int eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return null;
    }
}
