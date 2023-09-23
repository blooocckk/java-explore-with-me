package ru.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationOutputDto;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationOutputDto>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                                      @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                                      @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Received a request to get compilations with pinned={}, from={}, size={}", pinned, from, size);
        return ResponseEntity.ok(compilationService.getAll(pinned, from, size));
    }

    @GetMapping("/{compilationId}")
    public ResponseEntity<CompilationOutputDto> getCompilation(@PathVariable Long compilationId) {
        log.info("Received a request to get compilation with ID={}", compilationId);
        return ResponseEntity.ok(compilationService.get(compilationId));
    }

}
