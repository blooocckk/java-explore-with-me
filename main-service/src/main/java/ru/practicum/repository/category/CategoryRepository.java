package ru.practicum.repository.category;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.category.CategoryOutputDto;
import ru.practicum.model.category.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT NEW ru.practicum.dto.category.CategoryOutputDto(c.id, c.name) " +
            "FROM Category c " +
            "ORDER BY c.id ASC")
    List<CategoryOutputDto> getAll(Pageable pageable);
}
