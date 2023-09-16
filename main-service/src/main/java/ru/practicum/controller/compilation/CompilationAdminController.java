package ru.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationOutputDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationOutputDto> create(@Valid @RequestBody CompilationDto compilationDto) {
        log.info("Received a request to create a compilation: {}", compilationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(compilationService.create(compilationDto));
    }

    @DeleteMapping("/{compilationId}")
    public ResponseEntity<Void> delete(@PathVariable Long compilationId) {
        log.info("Received a request to delete a compilation with ID: {}", compilationId);
        compilationService.delete(compilationId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{compilationId}")
    public ResponseEntity<CompilationOutputDto> update(@PathVariable Long compilationId,
                                                       @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Received a request to update a compilation with ID {}: {}", compilationId, updateCompilationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(compilationService.update(compilationId, updateCompilationRequest));
    }
}
