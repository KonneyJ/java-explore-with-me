package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.RequestService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class RequestPrivateController {
    private final RequestService requestService;

    @GetMapping
    public Collection<ParticipationRequestDto> getAllRequests(@PathVariable("userId") int userId) {
        log.info("PRIVATE GET /users/{userId}/requests запрос with id {}", userId);
        return requestService.getAllRequests(userId);
    }

    @PostMapping
    public ParticipationRequestDto createRequest(@PathVariable("userId") int userId,
                                                 @PathVariable("eventId") int eventId) {
        log.info("PRIVATE POST /users/{userId}/requests запрос with userId {}, eventId {}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") int userId,
                                                 @PathVariable("requestId") int requestId) {
        log.info("PRIVATE PATCH /users/{userId}/requests/{requestId}/cancel запрос with userId {}, requestId {}",
                userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }
}
