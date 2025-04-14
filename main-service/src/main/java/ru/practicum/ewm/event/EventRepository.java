package ru.practicum.ewm.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    boolean findByCategoryId(int catId);

    List<Event> findByInitiatorId(int userId, PageRequest page);
}
