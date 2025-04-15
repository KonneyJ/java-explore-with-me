package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.event.model.Event;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Integer> {//, QuerydslPredicateExecutor<Event> {

    boolean findByCategoryId(int catId);

    List<Event> findByInitiatorId(int userId, PageRequest page);

    Set<Event> findAllByIdIn(Set<Integer> ids);
}
