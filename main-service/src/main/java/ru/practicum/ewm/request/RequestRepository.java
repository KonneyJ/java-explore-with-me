package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    List<Request> findAllByRequesterId(int userId);

    Optional<Request> findByIdAndRequesterId(int requestId, int userId);

    Request findByRequesterIdAndEventId(int userId, int eventId);

    List<Request> findAllByEventId(int eventId);

    List<Request> findAllByIdIn(List<Long> ids);
}

