package ru.practicum.main_service.categories.service;

import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(Integer catId, CategoryDto categoryDto);

    void deleteCategory(Integer catId);
}
