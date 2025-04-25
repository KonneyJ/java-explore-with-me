package ru.practicum.ewm.event.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.event.model.enums.EventState;
import ru.practicum.ewm.event.model.enums.StateActionAdmin;
import ru.practicum.ewm.event.model.enums.StateActionUser;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.IncorrectParamException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final StatsClient statsClient;
    private final ObjectMapper viewMapper;

    @Override
    public Collection<EventFullDto> getAllEventsByAdmin(List<Integer> users, List<EventState> states,
                                                        List<Integer> categories, LocalDateTime rangeStart,
                                                        LocalDateTime rangeEnd, int from, int size) {
        log.info("Поиск всех событий администратором");
        if (rangeStart != null && rangeEnd != null && (rangeStart.isAfter(rangeEnd))) {
            throw new IncorrectParamException("Дата начала поиска не может быть после даты окончания поиска");
        }
        List<Event> events;
        PageRequest page = PageRequest.of(from, size, Sort.by("eventDate").ascending());
        BooleanBuilder builder = new BooleanBuilder();

        if (users != null && !users.isEmpty()) {
            builder.and(QEvent.event.initiator.id.in(users));
        }

        if (states != null && !states.isEmpty()) {
            builder.and(QEvent.event.state.in(states));
        }

        if (categories != null && !categories.isEmpty()) {
            builder.and(QEvent.event.category.id.in(categories));
        }

        if (rangeStart != null) {
            builder.and(QEvent.event.eventDate.after(rangeStart));
        }

        if (rangeEnd != null) {
            builder.and(QEvent.event.eventDate.before(rangeEnd));
        }

        if (rangeStart == null && rangeEnd == null) {
            builder.and(QEvent.event.eventDate.after(LocalDateTime.now()));
        }

        if (builder.getValue() != null) {
            events = eventRepository.findAll(builder.getValue(), page).getContent();
        } else {
            events = eventRepository.findAll(page).getContent();
        }

        getViews(events);

        log.info("События успешно найдены");
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest updateEvent) {
        log.info("Обновление события с id {} администратором", eventId);
        Event event = checkEventExist(eventId);

        if (updateEvent.getEventDate() != null) {
            if (updateEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new IncorrectParamException("Дата начала события не соответствует, установленым правилам");
            }
        }

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ConflictException("Событие можно редактировать только если оно находится в состоянии" +
                    " ожидания публикации");
        }

        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(StateActionAdmin.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            } else {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
        }

        Event updatedEvent = updateFields(eventMapper.toUpdateEventRequest(updateEvent), event);
        Event savedEvent = eventRepository.save(updatedEvent);

        EventFullDto eventFullDto = eventMapper.toEventFullDto(savedEvent);
        log.info("Событие успешно обновлено {}", eventFullDto);
        return eventFullDto;
    }


    @Override
    public Collection<EventShortDto> getAllEvents(String text, List<Integer> categories, Boolean paid,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                  Boolean onlyAvailable, String sort, int from, int size) {
        log.info("Поиск всех событий");
        if (rangeStart != null && rangeEnd != null && (rangeStart.isAfter(rangeEnd))) {
            throw new IncorrectParamException("Дата начала поиска не может быть после даты окончания поиска");
        }

        List<Event> events;
        PageRequest page = PageRequest.of(from, size, Sort.by("eventDate").ascending());
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(QEvent.event.state.eq(EventState.PUBLISHED));

        if (text != null && !text.isBlank()) {
            builder.and(QEvent.event.annotation.containsIgnoreCase(text.toLowerCase())
                    .or(QEvent.event.description.containsIgnoreCase(text.toLowerCase())));
        }

        if (categories != null && !categories.isEmpty()) {
            builder.and(QEvent.event.category.id.in(categories));
        }

        if (paid != null) {
            builder.and(QEvent.event.paid.eq(paid));
        }

        if (rangeStart != null) {
            builder.and(QEvent.event.eventDate.after(rangeStart));
        }

        if (rangeEnd != null) {
            builder.and(QEvent.event.eventDate.before(rangeEnd));
        }

        if (onlyAvailable) {
            builder.and(QEvent.event.participantLimit.eq(0))
                    .or(QEvent.event.participantLimit.gt(QEvent.event.confirmedRequests));
        }

        if (builder.getValue() != null) {
            events = eventRepository.findAll(builder.getValue(), page).getContent();
        } else {
            events = eventRepository.findAll(page).getContent();
        }

        getViews(events);

        if (sort != null && sort.equals("VIEWS")) {
            events.stream()
                    .sorted(Comparator.comparingInt(Event::getViews))
                    .collect(Collectors.toList());
        }

        log.info("События успешно найдены");
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }


    @Override
    public EventFullDto getEventById(int eventId) {
        log.info("Поиск события с id {}", eventId);
        Event event = checkEventExist(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Событие с id = " + eventId + " еще не опубликовано");
        }
        getViews(List.of(event));
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        log.info("Событие успешно найдено {}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public Collection<EventShortDto> getAllEventsByUser(int userId, int from, int size) {
        log.info("Поиск всех событий пользователя с id {}, from {}, size {}", userId, from, size);
        checkUserExist(userId);
        PageRequest page = PageRequest.of(from, size, Sort.by("id").ascending());
        List<Event> events = eventRepository.findByInitiatorId(userId, page);
        getViews(events);
        log.info("События успешно найдены");
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(int userId, NewEventDto newEventDto) {
        log.info("Создание нового события {}", newEventDto);
        User user = checkUserExist(userId);
        Category category = checkCategoryExist(newEventDto.getCategory());
        checkTime(newEventDto.getEventDate());

        Event event = eventMapper.toEvent(newEventDto);
        event.setCategory(category);
        event.setConfirmedRequests(0);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(user);
        event.setViews(0);

        if (newEventDto.getLocation() != null) {
            Location location = locationRepository.save(newEventDto.getLocation());
            event.setLocation(location);
        }

        event.setState(EventState.PENDING);
        Event savedEvent = eventRepository.save(event);
        log.info("Событие успешно сохранено с id {}", savedEvent.getId());
        EventFullDto eventFullDto = eventMapper.toEventFullDto(savedEvent);
        return eventFullDto;
    }

    @Override
    public EventFullDto getEventByIdByUser(int userId, int eventId) {
        log.info("Поиск события с id {} пользователем с id {}", eventId, userId);
        User user = checkUserExist(userId);
        Event event = checkEventExist(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("Событие с id = " + eventId + " не принадлежит пользователю с id = " + userId);
        }
        getViews(List.of(event));
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        log.info("Событие успешно найдено {}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public EventFullDto updateEventByUser(int userId, int eventId, UpdateEventUserRequest updateEvent) {
        log.info("Обновление события с id {} пользователем с id {} ", eventId, userId);
        User user = checkUserExist(userId);
        Event event = checkEventExist(eventId);

        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("Событие с id = " + eventId + " не принадлежит пользователю с id = " + userId);
        }

        if (updateEvent.getEventDate() != null) {
            checkTime(updateEvent.getEventDate());
        }

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Событие можно редактировать только если оно еще не опубликовано");
        }

        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(StateActionUser.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            } else {
                event.setState(EventState.PENDING);
            }
        }

        Event updatedEvent = updateFields(eventMapper.toUpdateEventRequest(updateEvent), event);
        Event savedEvent = eventRepository.save(updatedEvent);

        getViews(List.of(savedEvent));
        EventFullDto eventFullDto = eventMapper.toEventFullDto(savedEvent);
        log.info("Событие успешно обновлено {}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public Collection<ParticipationRequestDto> getAllEventsRequestsByUser(int userId, int eventId) {
        log.info("Поиск всех заявок на событие с id {} пользователя с id {}", eventId, userId);
        User user = checkUserExist(userId);
        Event event = checkEventExist(eventId);

        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("Событие с id = " + eventId + " не принадлежит пользователю с id = " + userId);
        }

        List<Request> requests = requestRepository.findAllByEventId(eventId);
        log.info("Заявки на событие успешно найдены");
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequestByUser(int userId, int eventId,
                                                                   EventRequestStatusUpdateRequest updateRequest) {
        log.info("Подтверждение/отмена заявок на участие в событии с id {} пользователем с id {}, updateRequest {}",
                eventId, userId, updateRequest);
        User user = checkUserExist(userId);
        Event event = checkEventExist(eventId);

        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("Событие с id = " + eventId + " не принадлежит пользователю с id = " + userId);
        }
        log.info("Событие с id = " + eventId + " принадлежит пользователю с id = " + userId);

        int participantLimit = event.getParticipantLimit();
        if (participantLimit == 0 && event.getRequestModeration().equals(Boolean.FALSE)) {
            throw new ConflictException("Подтверждение заявок не требуется");
        }
        log.info("Требуется подтверждение заявок, т.к. лимит участников = {}, requestModeration {} ",
                participantLimit, event.getRequestModeration());

        if (participantLimit == event.getConfirmedRequests()) {
            throw new ConflictException("Лимит участников исчерпан. Нельзя подтвердить заявку");
        }
        log.info("Лимит участников не исчерпан, можно подтвердить заявки. Количество подтвержденных заявок на данный" +
                " момент = " + event.getConfirmedRequests());

        List<Request> requests = requestRepository.findAllByIdIn(updateRequest.getRequestIds());
        log.info("Запросы выгружены из БД {}", requests);
        if (!requests.isEmpty()) {
            if (requests.stream()
                    .map(Request::getStatus)
                    .anyMatch(status -> !status.equals(RequestStatus.PENDING))) {
                throw new ConflictException("Подтверждать заявки можно только в статусе ожидания");
            }
        }
        log.info("Заявки находятся в статусе ожидания. Подтверждение возможно");

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        RequestStatus status = updateRequest.getStatus();
        log.info("Подготовка к обработке заявок: сonfirmedRequests {}, rejectedRequests {}, status {}",
                confirmedRequests.size(), rejectedRequests.size(), status.toString());

        for (Request request : requests) {
            log.info("Запрос {}", request);
            if (event.getConfirmedRequests() < participantLimit) {
                log.info("Количество подтвержденных заявок = {} меньше лимита участников {}",
                        event.getConfirmedRequests(), participantLimit);
                switch (status) {
                    case CONFIRMED:
                        log.info("Статус Confirmed");
                        request.setStatus(RequestStatus.CONFIRMED);
                        log.info("Установили запросу статус Confirmed {}", request.getStatus());
                        ParticipationRequestDto confirmedDto = requestMapper.toDto(request);
                        log.info("Преобразовали запрос в dto {}", confirmedDto);
                        confirmedRequests.add(confirmedDto);
                        log.info("Добавили запрос в список подтвержденных запросов {}", confirmedRequests);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        log.info("Добавили +1 к числу подтвержденных запросов событию с id {}, кол-во подт.запросов = {}",
                                eventId, event.getConfirmedRequests());
                        break;
                    case REJECTED:
                        log.info("Статус Rejected");
                        request.setStatus(RequestStatus.REJECTED);
                        log.info("Установили запросу статус Rejected {}", request.getStatus());
                        ParticipationRequestDto rejectedDto = requestMapper.toDto(request);
                        log.info("Преобразовали запрос в dto {}", rejectedDto);
                        rejectedRequests.add(rejectedDto);
                        log.info("Добавили запрос в список отклоненных запросов {}", rejectedRequests);
                        break;
                    default:
                        log.info("Неизвестный параметр статуса {}", status.toString());
                        throw new IncorrectParamException("Некорректный параметр статуса");
                }
            } else {
                throw new ConflictException("Лимит участников исчерпан. Нельзя подтвердить остальные заявки");
                /*log.info("Количество подтвержденных заявок = {} равно лимиту участников {}",
                        event.getConfirmedRequests(), participantLimit);
                request.setStatus(RequestStatus.REJECTED);
                log.info("Установили запросу статус Rejected {}", request.getStatus());
                ParticipationRequestDto rejectedDto = requestMapper.toDto(request);
                log.info("Преобразовали запрос в dto {}", rejectedDto);
                rejectedRequests.add(rejectedDto);
                log.info("Добавили запрос в список отклоненных запросов {}", rejectedRequests);*/
            }
        }
        eventRepository.save(event);
        log.info("Сохранили в БД обновленное событие. Кол-во подтвержденных заявок {}", event.getConfirmedRequests());
        requestRepository.saveAll(requests);
        log.info("Сохранили в БД обновленные заявки {}", requests);
        EventRequestStatusUpdateResult updateResult = new EventRequestStatusUpdateResult(confirmedRequests,
                rejectedRequests);
        log.info("Заявки на участие успешно обновлены {}", updateResult);
        return updateResult;

    }


    private User checkUserExist(int userId) {
        log.info("Проверка пользователя с id {}", userId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        log.info("Пользователь успешно найден");
        return user;
    }

    private Category checkCategoryExist(int catId) {
        log.info("Проверка категории с id {}", catId);
        Category category = categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Категория с id = " + catId + " не найдена"));
        log.info("Категория успешно найдена");
        return category;
    }

    private void checkTime(LocalDateTime eventDate) {
        log.info("Проверка времени начала события eventDate {}", eventDate);
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IncorrectParamException("Дата начала события не соответствует, установленым правилам");
        }
        log.info("Дата начала события прошла модерацию");
    }

    private Event checkEventExist(int eventId) {
        log.info("Проверка события с id {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие с id = " + eventId + " не найдено"));
        log.info("Событие успешно найдено");
        return event;
    }

    private void getViews(List<Event> events) {
        log.info("Получение информации о просмотрах из сервиса статистики");
        if (events.isEmpty()) {
            return;
        }

        List<String> uris = events.stream()
                .map(event -> String.format("/events/%s", event.getId()))
                .collect(Collectors.toList());
        String start = LocalDateTime.now().minusYears(20).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("Отправка запроса с параметрами: start {}, end {}, uris {}", start, end, uris);
        ResponseEntity<Object> response = statsClient.getStats(start, end, uris, true);
        log.info("Получили response из сервиса статистики {}", response);

        List<ViewStatsDto> viewsList = new ArrayList<>();
        Map<Integer, Integer> viewsMap;
        try {
            viewsList = viewMapper.convertValue(response.getBody(), new TypeReference<>() {
            });
            log.info("Получили список просмотров из сервиса статистики {}", viewsList);
        } catch (IllegalArgumentException e) {
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new IncorrectParamException("Некорректный статус - " + response.getStatusCode());
            }
        }

        viewsMap = viewsList.stream()
                .filter(dto -> dto.getUri().startsWith("/events/"))
                .collect(Collectors.toMap(
                        dto -> Integer.parseInt(dto.getUri().substring("/events/".length())),
                        ViewStatsDto::getHits
                ));
        log.info("Сформировали хэшмапу с айдишниками и количеством просмотров {}", viewsMap);

        for (Event event : events) {
            log.info("Инициализация поля views = {} для event с id {} значением {}", event.getViews(), event.getId(), viewsMap.getOrDefault(event.getId(), 0));
            event.setViews(viewsMap.getOrDefault(event.getId(), 0));
        }
    }

    private Event updateFields(UpdateEventRequest updateEvent, Event event) {
        log.info("Обновление полей события {} новыми значениями из события {}", event, updateEvent);
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updateEvent.getAnnotation());
        }

        if (updateEvent.getCategory() != null) {
            Category category = checkCategoryExist(updateEvent.getCategory());
            event.setCategory(category);
        }

        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            event.setDescription(updateEvent.getDescription());
        }

        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }

        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }

        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }

        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }

        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }

        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
        log.info("Поля события успешно обновлены {}", event);
        return event;
    }
}
