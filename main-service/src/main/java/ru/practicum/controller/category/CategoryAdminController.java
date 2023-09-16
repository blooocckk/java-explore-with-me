package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryOutputDto;
import ru.practicum.service.category.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryOutputDto> create(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Received a request to create a category: {}", categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(categoryDto));
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryOutputDto> update(@PathVariable Long categoryId,
                                                    @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Received a request to update a category by ID: {}", categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.update(categoryDto, categoryId));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long categoryId) {
        log.info("Received a request to delete a category by ID: {}", categoryId);
        categoryService.delete(categoryId);
        return ResponseEntity.noContent().build();
    }
}