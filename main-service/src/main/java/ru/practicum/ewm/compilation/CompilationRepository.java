package ru.practicum.ewm.compilation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    List<Compilation> findAllByPinned(Boolean pinned, PageRequest page);
}
