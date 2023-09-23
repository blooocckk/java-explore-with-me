package ru.practicum.mapper.category;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryOutputDto;
import ru.practicum.model.category.Category;

@UtilityClass
public class CategoryMapper {
    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .name(category.getName())
                .build();
    }

    public CategoryOutputDto toOutputDto(Category category) {
        return CategoryOutputDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toEntity(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }
}
