package ru.practicum.ewm.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Like;

public interface LikeRepository extends JpaRepository<Like, Integer> {
}
