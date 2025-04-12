package ru.practicum.ewm.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean findByEmail(String email);

    Page<User> findByIdIn(List<Integer> ids, PageRequest page);
}
