package ru.practicum.ewm.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.like.LikeFullDto;
import ru.practicum.ewm.event.dto.like.LikeShortDto;
import ru.practicum.ewm.event.dto.like.NewLikeDto;
import ru.practicum.ewm.event.service.LikeService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
public class LikePrivateController {
    private final LikeService likeService;

    @PostMapping("/events/{eventId}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public LikeFullDto createLike(@PathVariable("userId") int userId,
                                  @PathVariable("eventId") int eventId,
                                  @Valid @RequestBody NewLikeDto newLikeDto) {
        log.info("PRIVATE POST /users/{userId}/events/{eventId}/likes запрос with userId {}, eventId {}, newLikeDto {}",
                userId, eventId, newLikeDto);
        return likeService.createLike(userId, eventId, newLikeDto);
    }

    @DeleteMapping("/events/{eventId}/likes/{likeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable("userId") int userId,
                           @PathVariable("eventId") int eventId,
                           @PathVariable("likeId") int likeId) {
        log.info("PRIVATE DELETE /users/{userId}/events/{eventId}/likes/{likeId} запрос with userId {}, eventId {}," +
                " likeId {}", userId, eventId, likeId);
        likeService.deleteLike(userId, eventId, likeId);
    }

    @PatchMapping("/events/{eventId}/likes/{likeId}")
    public LikeFullDto updateLike(@PathVariable("userId") int userId,
                                  @PathVariable("eventId") int eventId,
                                  @PathVariable("likeId") int likeId) {
        log.info("PRIVATE PATCH /users/{userId}/events/{eventId}/likes/{likeId} запрос with userId {}, eventId {}," +
                " likeId {}", userId, eventId, likeId);
        return likeService.updateLike(userId, eventId, likeId);
    }

    @GetMapping("/events/{eventId}/likes/{likeId}")
    public LikeFullDto getLikeById(@PathVariable("userId") int userId,
                                   @PathVariable("eventId") int eventId,
                                   @PathVariable("likeId") int likeId) {
        log.info("PRIVATE GET /users/{userId}/events/{eventId}/likes/{likeId} запрос with userId {}, eventId {}," +
                " likeId {}", userId, eventId, likeId);
        return likeService.getLikeById(userId, eventId, likeId);
    }

    @GetMapping("/events/{eventId}/likes")
    public Collection<LikeShortDto> getAllLikesByEvent(@PathVariable("userId") int userId,
                                                       @PathVariable("eventId") int eventId) {
        log.info("PRIVATE GET /users/{userId}/events/{eventId}/likes запрос with userId {}, eventId {}", userId, eventId);
        return likeService.getAllLikesByEvent(userId, eventId);
    }

    @GetMapping("/likes")
    public Collection<LikeShortDto> getAllLikesByUser(@PathVariable("userId") int userId) {
        log.info("PRIVATE GET /users/{userId}/likes запрос with userId {}", userId);
        return likeService.getAllLikesByUser(userId);
    }
}
