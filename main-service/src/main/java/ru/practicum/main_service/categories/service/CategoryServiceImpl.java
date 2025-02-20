package ru.practicum.main_service.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;
import ru.practicum.main_service.categories.mapper.CategoryMapper;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.categories.repository.CategoryRepository;
import ru.practicum.main_service.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(
                CategoryMapper.toModelByNew(newCategoryDto)));
    }

    @Override
    public CategoryDto updateCategory(Integer catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Integer catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
        categoryRepository.delete(category);
    }
}
