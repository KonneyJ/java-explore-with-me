package ru.practicum.ewm.event.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.like.LikeFullDto;
import ru.practicum.ewm.event.dto.like.LikeShortDto;
import ru.practicum.ewm.event.dto.like.NewLikeDto;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {
    @Override
    public LikeFullDto createLike(int userId, int eventId, NewLikeDto newLikeDto) {
        return null;
    }

    @Override
    public void deleteLike(int userId, int eventId, int likeId) {

    }

    @Override
    public LikeFullDto updateLike(int userId, int eventId, int likeId) {
        return null;
    }

    @Override
    public LikeFullDto getLikeById(int userId, int eventId, int likeId) {
        return null;
    }

    @Override
    public Collection<LikeShortDto> getAllLikesByEvent(int userId, int eventId) {
        return List.of();
    }

    @Override
    public Collection<LikeShortDto> getAllLikesByUser(int userId) {
        return List.of();
    }
}
