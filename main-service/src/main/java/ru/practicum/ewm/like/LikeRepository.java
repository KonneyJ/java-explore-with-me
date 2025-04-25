package ru.practicum.ewm.like;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Integer> {

    List<Like> findAllByEventId(int eventId, PageRequest page);

    List<Like> findAllByUserId(int userId, PageRequest page);

}
