package ru.practicum.ewm.like.dto;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.like.Like;

@Component
public class LikeMapper {
    public static LikeFullDto toFullDto(Like like) {
        return LikeFullDto.builder()
                .id(like.getId())
                .liked(like.getLiked())
                .user(like.getUser().getId())
                .event(like.getEvent().getId())
                .request(like.getRequest().getId())
                .createdOn(like.getCreatedOn())
                .build();
    }

    public static LikeShortDto toShortDto(Like like) {
        return LikeShortDto.builder()
                .id(like.getId())
                .liked(like.getLiked())
                .createdOn(like.getCreatedOn())
                .build();
    }
}
