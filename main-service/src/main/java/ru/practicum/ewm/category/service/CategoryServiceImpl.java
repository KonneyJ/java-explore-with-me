package ru.practicum.ewm.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.exceptions.NotFoundException;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return null;
    }

    @Override
    public void deleteCategory(int catId) {
        log.info("Удаление категории с id {}", catId);
        Category category = categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Категория с id = " + catId + " не найден"));
        categoryRepository.deleteById(catId);
        log.info("Категория успешно удалена");
    }

    @Override
    public CategoryDto updateCategory(int catId, CategoryDto categoryDto) {
        return null;
    }

    @Override
    public Collection<CategoryDto> getAllCategories(int from, int size) {
        return List.of();
    }

    @Override
    public CategoryDto getCategoryById(int catId) {
        return null;
    }
}
