package ru.practicum.service.category;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryOutputDto;

import java.util.List;

public interface CategoryService {
    CategoryOutputDto create(CategoryDto categoryDto);

    void delete(Long userId);

    CategoryOutputDto update(CategoryDto categoryDto, Long categoryId);

    List<CategoryOutputDto> getAll(Integer from, Integer size);

    CategoryOutputDto getById(Long categoryId);
}
