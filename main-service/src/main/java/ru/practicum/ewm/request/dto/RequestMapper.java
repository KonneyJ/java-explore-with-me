package ru.practicum.ewm.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.request.model.Request;

@Component
public class RequestMapper {

    public static ParticipationRequestDto toDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
