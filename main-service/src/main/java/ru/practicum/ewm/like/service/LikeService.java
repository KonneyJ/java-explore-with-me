package ru.practicum.ewm.like.service;

import ru.practicum.ewm.like.dto.LikeFullDto;
import ru.practicum.ewm.like.dto.LikeShortDto;
import ru.practicum.ewm.like.dto.NewLikeDto;

import java.util.Collection;

public interface LikeService {

    LikeFullDto createLike(int userId, int eventId, NewLikeDto newLikeDto);

    void deleteLike(int userId, int eventId, int likeId);

    LikeFullDto updateLike(int userId, int eventId, int likeId);

    LikeFullDto getLikeById(int userId, int eventId, int likeId);

    Collection<LikeShortDto> getAllLikesByEvent(int userId, int eventId, int from, int size);

    Collection<LikeShortDto> getAllLikesByUser(int userId, int from, int size);
}
