package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryOutputDto;
import ru.practicum.service.category.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Collection<CategoryOutputDto>> getAll(@RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                                @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Received a request to fetch categories. From: {}, Size: {}", from, size);
        return ResponseEntity.ok(categoryService.getAll(from, size));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryOutputDto> getById(@PathVariable Long categoryId) {
        log.info("Received a request to fetch a category by ID: {}", categoryId);
        return ResponseEntity.ok(categoryService.getById(categoryId));
    }
}
