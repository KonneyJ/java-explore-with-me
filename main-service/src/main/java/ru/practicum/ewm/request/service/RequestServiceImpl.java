package ru.practicum.ewm.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.enums.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.IncorrectParamException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final EventRepository eventRepository;

    @Override
    public Collection<ParticipationRequestDto> getAllRequests(int userId) {
        log.info("Поиск всех заявок пользователя с id {}", userId);
        User user = checkUserExist(userId);
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        log.info("Заявки успешно найдены");
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        log.info("Создание заявки на событие с id {} пользователем с id {}", eventId, userId);
        User user = checkUserExist(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие с id = " + eventId + " не найдено"));

        if (requestRepository.findByRequesterIdAndEventId(userId, eventId) != null) {
            throw new ConflictException("Заявка уже существует. Нельзя добавить повторный запрос");
        }

        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("Организатор события не может подавать заявку на это событие");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Подавать заявку может только на опубликованные события");
        }

        int confirmedRequests = event.getConfirmedRequests();
        if (event.getParticipantLimit().equals(confirmedRequests) && (event.getParticipantLimit() > 0)) {
            throw new ConflictException("Достигнут лимит запросов на участие");
        }

        Request request = new Request();
        if (event.getRequestModeration() && (event.getParticipantLimit() != 0)) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(confirmedRequests + 1);
            Event savedEvent = eventRepository.save(event);
            log.info("Добавлена 1 подтвержденная заявка на участие. Было заявок {}, стало {}",
                    confirmedRequests, savedEvent.getConfirmedRequests());
        }

        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);
        Request savedRequest = requestRepository.save(request);
        log.info("Заявка на событие успешно сохранена с id {}", savedRequest.getId());
        return requestMapper.toDto(savedRequest);
    }

    @Override
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        log.info("Отмена заявки с id {} пользователем с id {}", requestId, userId);
        User user = checkUserExist(userId);
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(
                () -> new NotFoundException("Заявка с id = " + requestId + " не найдена"));

        if (request.getStatus().equals(RequestStatus.CANCELED) || request.getStatus().equals(RequestStatus.REJECTED)) {
            throw new IncorrectParamException("Запрос уже отклонен");
        }

        request.setStatus(RequestStatus.CANCELED);
        log.info("Обновление запроса в БД");
        Request savedRequest = requestRepository.save(request);
        ParticipationRequestDto dto = requestMapper.toDto(savedRequest);
        log.info("Отмена заявки успешно осуществлена {}", dto);
        return dto;
    }

    private User checkUserExist(int userId) {
        log.info("Проверка пользователя с id {}", userId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        log.info("Пользователь успешно найден");
        return user;
    }
}
