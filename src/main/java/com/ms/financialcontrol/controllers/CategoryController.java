package com.ms.financialcontrol.controllers;

import com.ms.financialcontrol.dtos.CategoryDto;
import com.ms.financialcontrol.exceptions.CategoryNotFoundException;
import com.ms.financialcontrol.models.CategoryModel;
import com.ms.financialcontrol.services.CategoryService;
import lombok.AllArgsConstructor;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final Logger logger = LoggerFactory.getLogger(RevenueController.class);
    private static final String CATEGORY_FOUND = "category {} found";

    @PostMapping
    public ResponseEntity<Object> saveCategory(@RequestBody @Valid CategoryDto categoryDto) {
        if(categoryService.existsByName(categoryDto.getName())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(String.format("Conflict: Name %s is already in use!", categoryDto.getName()));
        }

        var categoryModel = new CategoryModel();
        BeanUtils.copyProperties(categoryDto, categoryModel);
        logger.debug("categoryModel mapped");
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(categoryModel));
    }

    @GetMapping
    public ResponseEntity<Page<CategoryModel>> getAllCategories(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable(value = "id") UUID id) {
        CategoryModel categoryRequest = categoryService.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        logger.debug(CATEGORY_FOUND, categoryRequest.getName());
        return ResponseEntity.status(HttpStatus.OK).body(categoryRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable(value = "id") UUID id, @RequestBody @Valid CategoryDto categoryDto) {
        CategoryModel categoryRequest = categoryService.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        logger.debug(CATEGORY_FOUND, categoryRequest.getName());
        var categoryModel = new CategoryModel();
        BeanUtils.copyProperties(categoryDto, categoryModel);
        logger.debug("categoryModel mapped");
        categoryModel.setId(categoryRequest.getId());
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.saveCategory(categoryModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable(value = "id") UUID id) {
        CategoryModel categoryModel = categoryService.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        logger.debug(CATEGORY_FOUND, categoryModel.getName());
        categoryService.delete(categoryModel);
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Category id %s deleted successfully!", id));
    }
}
