package ru.practicum.ewm.compilation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.Compilation;
import ru.practicum.ewm.compilation.CompilationRepository;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.NotFoundException;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto getCompilationById(int compId) {
        log.info("Поиск подборки событий с id {}", compId);
        Compilation compilation = checkCompilationExist(compId);
        return compilationMapper.toDto(compilation);
    }

    @Override
    public Collection<CompilationDto> getAllCompilations(Boolean pinned, int from, int size) {
        log.info("Поиск всех подборок событий с pinned {}, from {}, size {}", pinned, from, size);
        PageRequest page = PageRequest.of(from, size);
        List<Compilation> compilations;

        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, page);
        } else {
            compilations = compilationRepository.findAll(page).getContent();
        }

        log.info("Подборки событий успешно найдены");
        return compilations.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        log.info("Создание подборки событий {}", newCompilationDto);
        Set<Event> events = new HashSet<>();

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
        }

        Compilation compilation = compilationMapper.toCompilation(newCompilationDto);
        compilation.setEvents(events);
        Compilation savedCompilation = compilationRepository.save(compilation);
        log.info("Подборка событий успешно сохранена с id {}", savedCompilation);
        return compilationMapper.toDto(savedCompilation);
    }

    @Override
    public void deleteCompilation(int compId) {
        log.info("Удаление подборки событий с id {}", compId);
        Compilation compilation = checkCompilationExist(compId);
        compilationRepository.deleteById(compId);
        log.info("Подборка событий успешно удалена");
    }

    @Override
    public CompilationDto updateCompilation(int compId, UpdateCompilationRequest updateRequest) {
        log.info("Обновление подборки событий с id {}", compId);
        Compilation compilation = checkCompilationExist(compId);

        if (updateRequest.getEvents() != null && !updateRequest.getEvents().isEmpty()) {
            Set<Event> events = eventRepository.findAllByIdIn(updateRequest.getEvents());
            compilation.setEvents(events);
        }

        if (updateRequest.getPinned() != null) {
            compilation.setPinned(updateRequest.getPinned());
        }

        if (updateRequest.getTitle() != null && !updateRequest.getTitle().isBlank()) {
            compilation.setTitle(updateRequest.getTitle());
        }

        Compilation savedCompilation = compilationRepository.save(compilation);
        log.info("Подборка событий успешно обновлена");
        return compilationMapper.toDto(savedCompilation);
    }

    private Compilation checkCompilationExist(int compId) {
        log.info("Проверка наличия подборки событий с id {}", compId);
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Подборка с id = " + compId + " не найдена"));
        log.info("Подборка событий успешно найдена");
        return compilation;
    }
}

