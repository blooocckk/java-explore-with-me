package ru.practicum.service.compilation;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationOutputDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationOutputDto create(CompilationDto compilationDto);

    void delete(Long compilationId);

    List<CompilationOutputDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationOutputDto get(Long compilationId);

    CompilationOutputDto update(Long compilationId, UpdateCompilationRequest updateCompilationRequest);
}
