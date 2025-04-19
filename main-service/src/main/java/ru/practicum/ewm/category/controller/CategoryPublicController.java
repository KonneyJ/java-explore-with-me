package ru.practicum.ewm.category.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                    @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("PUBLIC GET /categories запрос with from {}, size {}", from, size);
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable("catId") int catId) {
        log.info("PUBLIC GET /categories/catId запрос with id {}", catId);
        return categoryService.getCategoryById(catId);
    }
}
