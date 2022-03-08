package com.ms.financialcontrol.controllers;

import com.ms.financialcontrol.dtos.CategoryDto;
import com.ms.financialcontrol.models.CategoryModel;
import com.ms.financialcontrol.services.CategoryService;
import lombok.AllArgsConstructor;
import lombok.var;
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
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Object> saveCategory(@RequestBody @Valid CategoryDto categoryDto) {
        if(categoryService.existsByName(categoryDto.getName())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(String.format("Conflict: Name %s is already in use!", categoryDto.getName()));
        }

        var categoryModel = new CategoryModel();
        BeanUtils.copyProperties(categoryDto, categoryModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(categoryModel));
    }

    @GetMapping
    public ResponseEntity<Page<CategoryModel>> getAllCategories(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable(value = "id") Long id) {
        Optional<CategoryModel> categoryOptional = categoryService.findById(id);

        if (!categoryOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Category id %d not found!", id));
        }

        return ResponseEntity.status(HttpStatus.OK).body(categoryOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable(value = "id") Long id) {
        Optional<CategoryModel> categoryOptional = categoryService.findById(id);

        if (!categoryOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Category id %d not found!", id));
        }

        categoryService.delete(categoryOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Category id %d deleted successfully!", id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable(value = "id") Long id, @RequestBody @Valid CategoryDto categoryDto) {
        Optional<CategoryModel> categoryOptional = categoryService.findById(id);

        if (!categoryOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Category id %d not found!", id));
        }

        var categoryModel = new CategoryModel();
        BeanUtils.copyProperties(categoryDto, categoryModel);
        categoryModel.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.saveCategory(categoryModel));
    }
}
