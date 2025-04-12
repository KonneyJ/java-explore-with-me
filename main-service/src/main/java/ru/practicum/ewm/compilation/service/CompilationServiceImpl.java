package ru.practicum.ewm.compilation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.CompilationRepository;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public CompilationDto getCompilationById(int compId) {
        return null;
    }

    @Override
    public Collection<CompilationDto> getAllCompilations(boolean pinned, int from, int size) {
        return List.of();
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        return null;
    }

    @Override
    public void deleteCompilation(int compId) {

    }

    @Override
    public CompilationDto updateCompilation(int compId, UpdateCompilationRequest updateRequest) {
        return null;
    }
}
