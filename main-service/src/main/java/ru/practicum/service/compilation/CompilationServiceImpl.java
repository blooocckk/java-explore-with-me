package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationOutputDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.mapper.compilation.CompilationMapper;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.repository.compilation.CompilationRepository;
import ru.practicum.repository.event.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    private void setEventsForEntity(List<Long> eventsIds, Compilation compilation) {
        if (eventsIds != null && !eventsIds.isEmpty()) {
            List<Event> events = eventRepository.findAllById(eventsIds);
            compilation.setEvents(events);
        }
    }

    private void setEventsForDto(List<Event> events, CompilationOutputDto compilationOutputDto) {
        compilationOutputDto.setEvents(Collections.emptyList());
        if (events != null && !events.isEmpty()) {
            List<EventShortDto> eventShortDtoList = events.stream()
                    .map(EventMapper::toShortDto)
                    .collect(Collectors.toList());
            compilationOutputDto.setEvents(eventShortDtoList);
        }
    }

    @Override
    @Transactional
    public CompilationOutputDto create(CompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toEntity(compilationDto);
        setEventsForEntity(compilationDto.getEvents(), compilation);

        if (compilationDto.getPinned() == null) {
            compilation.setPinned(false);
        }

        Compilation savedCompilation = compilationRepository.save(compilation);

        CompilationOutputDto compilationOutputDto = CompilationMapper.toOutputDto(savedCompilation);
        setEventsForDto(savedCompilation.getEvents(), compilationOutputDto);
        log.info("Compilation created successfully: Compilation ID - {}", savedCompilation.getId());
        return compilationOutputDto;
    }

    @Override
    @Transactional
    public void delete(Long compilationId) {
        compilationRepository.deleteById(compilationId);
        log.info("Deleting compilation: Compilation ID - {}", compilationId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationOutputDto> getAll(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<CompilationOutputDto> events = new ArrayList<>();
        compilationRepository.findAllByPinned(pinned, pageable).forEach(compilation -> {
            CompilationOutputDto outputDto = CompilationMapper.toOutputDto(compilation);
            setEventsForDto(compilation.getEvents(), outputDto);
            events.add(outputDto);
        });
        log.info("Returning {} compilations", events.size());
        return events;
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationOutputDto get(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation not found"));

        CompilationOutputDto compilationOutputDto = CompilationMapper.toOutputDto(compilation);
        setEventsForDto(compilation.getEvents(), compilationOutputDto);
        log.info("Returning compilation by ID: Compilation ID - {}", compilationId);
        return compilationOutputDto;
    }

    @Override
    @Transactional
    public CompilationOutputDto update(Long compilationId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation not found"));

        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        Compilation updatedCompilation = compilationRepository.save(compilation);
        log.info("Compilation updated successfully: Compilation ID - {}", updatedCompilation.getId());
        return CompilationMapper.toOutputDto(updatedCompilation);
    }
}
