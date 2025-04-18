package ru.practicum.ewm.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.ForbiddenException;
import ru.practicum.ewm.exceptions.NotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        log.info("Проверка наличия категории с name {}", newCategoryDto.getName());
        checkUniqueName(newCategoryDto.getName());
        log.info("Добавление новой категории {}", newCategoryDto);
        Category category = categoryRepository.save(categoryMapper.toCategory(newCategoryDto));
        log.info("Категория успешно добавлена с id {}", category.getId());
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategory(int catId) {
        log.info("Удаление категории с id {}", catId);
        Category category = checkCategoryExist(catId);

        log.info("Проверка наличия событий привязанных к катеории");
        List<Event> events = eventRepository.findByCategory(category);
        if ((events != null) && (!events.isEmpty())) {
            throw new ConflictException("Нельзя удалить категорию, с которой связаны события");
        }

        categoryRepository.deleteById(catId);
        log.info("Категория успешно удалена");
    }

    @Override
    public CategoryDto updateCategory(int catId, CategoryDto categoryDto) {
        log.info("Проверка наличия категории перед обновлением");
        Category category = checkCategoryExist(catId);

        if (categoryDto.getName() != null && !categoryDto.getName().equals(category.getName())) {
            log.info("Проверка наличия категории с name {}", categoryDto.getName());
            checkUniqueName(categoryDto.getName());
        }

        log.info("Обновление категории");
        categoryDto.setId(category.getId());
        Category savedCategory = categoryRepository.save(categoryMapper.toCategory(categoryDto));

        log.info("Категория успешно добавлена с id {}", savedCategory.getId());
        return categoryMapper.toCategoryDto(savedCategory);
    }

    @Override
    public Collection<CategoryDto> getAllCategories(int from, int size) {
        log.info("Получение списка категорий с from {}, size {}", from, size);
        PageRequest page = PageRequest.of(from, size, Sort.by("id").ascending());
        List<Category> categories = categoryRepository.findAll(page).getContent();

        log.info("Категории успешно найдены");
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(int catId) {
        log.info("Поиск категории с id {}", catId);
        Category category = checkCategoryExist(catId);
        log.info("Категория с id {} успешно найдена {}", catId, category);
        return categoryMapper.toCategoryDto(category);
    }

    private Category checkCategoryExist(int catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Категория с id = " + catId + " не найдена"));
        return category;
    }

    private void checkUniqueName(String name) {
        if (categoryRepository.findByName(name) != null) {
            throw new ConflictException("Категория с name: " + name + " уже существует. Добавление невозможно");
        }
    }
}
