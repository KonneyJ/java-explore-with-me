package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface RequestService {

    Collection<ParticipationRequestDto> getAllRequests(int userId);

    ParticipationRequestDto createRequest(int userId, int eventId);

    ParticipationRequestDto cancelRequest(int userId, int requestId);
}
