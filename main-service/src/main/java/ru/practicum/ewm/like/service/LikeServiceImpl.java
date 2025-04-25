package ru.practicum.ewm.like.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.like.dto.LikeFullDto;
import ru.practicum.ewm.like.dto.LikeShortDto;
import ru.practicum.ewm.like.dto.NewLikeDto;
import ru.practicum.ewm.like.dto.LikeMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.like.Like;
import ru.practicum.ewm.event.model.enums.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.like.LikeRepository;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.IncorrectParamException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.request.RequestRepository;
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
public class LikeServiceImpl implements LikeService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;

    @Override
    public LikeFullDto createLike(int userId, int eventId, NewLikeDto newLikeDto) {
        log.info("Создание нового лайка {} пользователем с id {} на событие с id {}", newLikeDto, userId, eventId);
        User user = checkUserExist(userId);
        Event event = checkEventExist(eventId);

        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("Нельзя ставить лайк своему событию");
        }

        checkTime(event.getEventDate());

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Лайк можно поставить только опубликованному событию");
        }

        Request request = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (!request.getStatus().equals(RequestStatus.CONFIRMED)) {
            throw new ConflictException("Лайк можно поставить только посещенному мероприятию");
        }
        Like like = new Like();
        like.setLiked(newLikeDto.getLiked());
        like.setUser(user);
        like.setEvent(event);
        like.setRequest(request);
        like.setCreatedOn(LocalDateTime.now());
        Like savedLike = likeRepository.save(like);
        log.info("Лайк успешно сохранен с id {}", savedLike.getId());
        LikeFullDto likeFullDto = likeMapper.toFullDto(savedLike);
        return likeFullDto;
    }

    @Override
    public void deleteLike(int userId, int eventId, int likeId) {
        log.info("Удаление лайка с id {} пользователя с id {} на событие с id {}", likeId, userId, eventId);
        User user = checkUserExist(userId);
        Like like = checkLikeExist(likeId);

        if (like.getUser().getId() != userId) {
            throw new ConflictException("Пользователь может удалять только свои лайки");
        }
        likeRepository.deleteById(likeId);
        log.info("Лайк успешно удален");
    }

    @Override
    public LikeFullDto updateLike(int userId, int eventId, int likeId) {
        log.info("Обновление лайка с id {} пользователем с id {} на событие с id {}", likeId, userId, eventId);
        User user = checkUserExist(userId);
        Event event = checkEventExist(eventId);
        Like like = checkLikeExist(likeId);

        if (like.getUser().getId() != userId) {
            throw new ConflictException("Пользователь может обновлять только свои лайки");
        }
        checkTime(event.getEventDate());

        if (like.getLiked().equals(true)) {
            like.setLiked(false);
        } else {
            like.setLiked(true);
        }
        like.setCreatedOn(LocalDateTime.now());

        Like updatedLiked = likeRepository.save(like);
        LikeFullDto likeFullDto = likeMapper.toFullDto(updatedLiked);
        log.info("Лайк успешно обновлен {}", likeFullDto);
        return likeFullDto;
    }

    @Override
    public LikeFullDto getLikeById(int userId, int eventId, int likeId) {
        log.info("Поиск лайка с id {} пользователем с id {} на событие с id {}", likeId, userId, eventId);
        User user = checkUserExist(userId);
        Event event = checkEventExist(eventId);
        Like like = checkLikeExist(likeId);

        if (like.getUser().getId() != userId || event.getInitiator().getId() != userId) {
            throw new ConflictException("Лайк может посмотреть либо владелец события, либо человек, который его поставил");
        }
        LikeFullDto likeFullDto = likeMapper.toFullDto(like);
        log.info("Лайк успешно найден {}", likeFullDto);
        return likeFullDto;
    }

    @Override
    public Collection<LikeShortDto> getAllLikesByEvent(int userId, int eventId, int from, int size) {
        log.info("Поиск всех лайков события с id {} пользователем с id {} with from {] and size {}",
                eventId, userId, from, size);
        User user = checkUserExist(userId);
        Event event = checkEventExist(eventId);

        if (event.getInitiator().getId() != userId) {
            throw new ConflictException("Только владелец события может посмотреть все лайки");
        }

        PageRequest page = PageRequest.of(from, size, Sort.by("id").ascending());
        List<Like> likes = likeRepository.findAllByEventId(eventId, page);
        List<LikeShortDto> likesDto = likes.stream()
                .map(LikeMapper::toShortDto)
                .collect(Collectors.toList());
        log.info("Лайки события успешно найдены");
        return likesDto;
    }

    @Override
    public Collection<LikeShortDto> getAllLikesByUser(int userId, int from, int size) {
        log.info("Поиск лайков пользователя с id {} with from {} and size {}", userId, from, size);
        User user = checkUserExist(userId);
        PageRequest page = PageRequest.of(from, size, Sort.by("id").ascending());
        List<Like> likes = likeRepository.findAllByUserId(userId, page);
        List<LikeShortDto> likesDto = likes.stream()
                .map(LikeMapper::toShortDto)
                .collect(Collectors.toList());
        log.info("Лайки события успешно найдены");
        return likesDto;
    }

    private User checkUserExist(int userId) {
        log.info("Проверка пользователя с id {}", userId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        log.info("Пользователь успешно найден");
        return user;
    }

    private Event checkEventExist(int eventId) {
        log.info("Проверка события с id {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие с id = " + eventId + " не найдено"));
        log.info("Событие успешно найдено");
        return event;
    }

    private void checkTime(LocalDateTime eventDate) {
        log.info("Проверка времени лайка");
        if (eventDate.isBefore(LocalDateTime.now().minusHours(3))) {
            throw new IncorrectParamException("Лайк можно поставить только через 3 часа после начала события");
        }
        log.info("Дата лайка прошла модерацию");
    }

    private Like checkLikeExist(int likeId) {
        log.info("Проверка лайка с id {}", likeId);
        Like like = likeRepository.findById(likeId).orElseThrow(
                () -> new NotFoundException("Лайк с id = " + likeId + " не найден"));
        log.info("Лайк успешно найден");
        return like;
    }
}
