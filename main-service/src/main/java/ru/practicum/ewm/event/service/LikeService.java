package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.like.LikeFullDto;
import ru.practicum.ewm.event.dto.like.LikeShortDto;
import ru.practicum.ewm.event.dto.like.NewLikeDto;

import java.util.Collection;

public interface LikeService {

    LikeFullDto createLike(int userId, int eventId, NewLikeDto newLikeDto);

    void deleteLike(int userId, int eventId, int likeId);

    LikeFullDto updateLike(int userId, int eventId, int likeId);

    LikeFullDto getLikeById(int userId, int eventId, int likeId);

    Collection<LikeShortDto> getAllLikesByEvent(int userId, int eventId);

    Collection<LikeShortDto> getAllLikesByUser(int userId);
}
