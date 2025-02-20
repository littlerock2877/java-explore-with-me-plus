package ru.practicum.main_service.categories.mapper;

import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;
import ru.practicum.main_service.categories.model.Category;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(),
                category.getName());
    }

    public static Category toModelByNew(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public static Category toModel(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }
}
