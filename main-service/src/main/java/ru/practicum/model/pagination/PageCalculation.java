package ru.practicum.model.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageCalculation extends PageRequest {
    public PageCalculation(Integer from, Integer size, Sort sort) {
        super(calculateOffset(from, size), size, sort);
    }

    public PageCalculation(Integer from, Integer size) {
        super(calculateOffset(from, size), size, Sort.unsorted());
    }

    private static Integer calculateOffset(Integer from, Integer size) {
        return from / size;
    }
}
