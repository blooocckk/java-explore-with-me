package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryOutputDto;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.mapper.category.CategoryMapper;
import ru.practicum.model.category.Category;
import ru.practicum.repository.category.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryOutputDto create(CategoryDto categoryDto) {
        Category category = categoryRepository.save(CategoryMapper.toEntity(categoryDto));
        log.info("Category successfully created");
        return CategoryMapper.toOutputDto(category);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        categoryRepository.deleteById(userId);
        log.info("Category successfully deleted");
    }

    @Override
    @Transactional
    public CategoryOutputDto update(CategoryDto categoryDto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found"));

        String newName = categoryDto.getName();
        if (newName != null) {
            category.setName(newName);
        }

        log.info("Category successfully updated");
        return CategoryMapper.toOutputDto(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryOutputDto> getAll(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        log.info("Returning a list of all categories");
        return categoryRepository.getAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryOutputDto getById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found"));
        log.info("Returning found category");
        return CategoryMapper.toOutputDto(category);
    }
}
