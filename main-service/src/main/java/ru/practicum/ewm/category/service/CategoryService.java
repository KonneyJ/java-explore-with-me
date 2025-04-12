package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

import java.util.Collection;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(int catId);

    CategoryDto updateCategory(int catId, CategoryDto categoryDto);

    Collection<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryById(int catId);
}
