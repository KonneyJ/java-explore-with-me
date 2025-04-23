package ru.practicum.ewm.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.request.model.Request;

@Component
public class RequestMapper {

    public static ParticipationRequestDto toDto(Request request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
