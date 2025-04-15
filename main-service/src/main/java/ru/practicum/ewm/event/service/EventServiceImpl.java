package ru.practicum.ewm.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.enums.EventState;
import ru.practicum.ewm.event.model.enums.StateActionAdmin;
import ru.practicum.ewm.event.model.enums.StateActionUser;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.exceptions.ForbiddenException;
import ru.practicum.ewm.exceptions.IncorrectParamException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    @Override
    public Collection<EventFullDto> getAllEventsByAdmin(List<Integer> users, List<EventState> states,
                                                        List<Integer> categories, LocalDateTime rangeStart,
                                                        LocalDateTime rangeEnd, int from, int size) {
        return List.of();
    }

    @Override
    public EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest updateEvent) {
        log.info("Обновление события с id {} администратором", eventId);
        Event event = checkEventExist(eventId);

        if (updateEvent.getEventDate() != null) {
            if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ForbiddenException("Дата начала события не соответствует, установленым правилам");
            }
        }

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ForbiddenException("Событие можно редактировать только если оно находится в состоянии" +
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

        setViews(List.of(savedEvent));
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
        List<Event> events = new ArrayList<>();
        /*switch(sort) {
            case EVENT_DATE :
                PageRequest page = PageRequest.of(from, size, Sort.by("eventDate").ascending());
                break;
            case VIEWS :

        }*/
        PageRequest page = PageRequest.of(from, size, Sort.by("eventDate").ascending());
        BooleanBuilder builder = new BooleanBuilder();

        /*if (text != null && !text.isBlank()) {
            builder.and(QEvent.event.annotation.containsIgnoreCase(text.toLowerCase())
                    .or(QEvent.event.description.containsIgnoreCase(text.toLowerCase())));
        }

        if(categories != null && !categories.isEmpty()) {
            builder.and(QEvent.event.category.id.in(categories));
        }

        if(paid != null) {
            builder.and(QEvent.event.paid.eq(paid));
        }

        if(rangeStart != null) {
            builder.and(QEvent.event.eventDate.after(rangeStart));
        }

        if(rangeEnd != null) {
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
        }*/

        setViews(events);

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
        setViews(List.of(event));
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        log.info("Событие успешно найдено {}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public Collection<EventShortDto> getAllEventsByUser(int userId, int from, int size) {
        log.info("Поиск всех событий пользователя с id {}, from {}, size {}", userId, from, size);
        checkUserExist(userId);
        PageRequest page = PageRequest.of(from, size, Sort.by("event_id").ascending());
        List<Event> events = eventRepository.findByInitiatorId(userId, page);
        setViews(events);
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
        if (newEventDto.getLocation() != null) {
            Location location = locationRepository.save(newEventDto.getLocation());
            event.setLocation(location);
        }
        event.setState(EventState.PENDING);
        Event savedEvent = eventRepository.save(event);
        log.info("Событие успешно сохранено с id {}", savedEvent.getId());
        EventFullDto eventFullDto = eventMapper.toEventFullDto(savedEvent);
        eventFullDto.setViews(0);
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
        setViews(List.of(event));
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
            throw new ForbiddenException("Событие можно редактировать только если оно еще не опубликовано");
        }

        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(StateActionUser.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            } else {
                event.setState(EventState.PENDING);
            }
        }

        Event updatedEvent = updateFields(eventMapper.toUpdateEventRequest(updateEvent), event);

        setViews(List.of(updatedEvent));
        EventFullDto eventFullDto = eventMapper.toEventFullDto(updatedEvent);
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
                                                                   EventRequestStatusUpdateRequest
                                                                           eventRequestStatusUpdateRequest) {
        log.info("Подтверждение/отмена заявок на участие в событии с id {} пользователем с id {}", eventId, userId);
        User user = checkUserExist(userId);
        Event event = checkEventExist(eventId);

        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("Событие с id = " + eventId + " не принадлежит пользователю с id = " + userId);
        }

        return null;
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
            throw new ForbiddenException("Дата начала события не соответствует, установленым правилам");
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

    private void setViews(List<Event> events) {

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

        event.setEventDate(updateEvent.getEventDate());

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
