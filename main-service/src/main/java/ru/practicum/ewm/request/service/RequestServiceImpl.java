package ru.practicum.ewm.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private  final RequestRepository requestRepository;

    @Override
    public Collection<ParticipationRequestDto> getAllRequests(int userId) {
        return List.of();
    }

    @Override
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        return null;
    }

    @Override
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        return null;
    }
}
