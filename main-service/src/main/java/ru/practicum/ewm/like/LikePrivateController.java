package ru.practicum.ewm.like;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.like.dto.LikeFullDto;
import ru.practicum.ewm.like.dto.LikeShortDto;
import ru.practicum.ewm.like.dto.NewLikeDto;
import ru.practicum.ewm.like.service.LikeService;

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
                                                       @PathVariable("eventId") int eventId,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                       @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("PRIVATE GET /users/{userId}/events/{eventId}/likes запрос with userId {}, eventId {}, from {}, " +
                "size {}", userId, eventId, from, size);
        return likeService.getAllLikesByEvent(userId, eventId, from, size);
    }

    @GetMapping("/likes")
    public Collection<LikeShortDto> getAllLikesByUser(@PathVariable("userId") int userId,
                                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                      @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("PRIVATE GET /users/{userId}/likes запрос with userId {}, from {}, size {}", userId, from, size);
        return likeService.getAllLikesByUser(userId, from, size);
    }
}
