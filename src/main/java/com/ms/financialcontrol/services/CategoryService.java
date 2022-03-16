package com.ms.financialcontrol.services;

import com.ms.financialcontrol.exceptions.CategoryConflictException;
import com.ms.financialcontrol.models.CategoryModel;
import com.ms.financialcontrol.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public boolean existsById(UUID id) { return categoryRepository.existsById(id); }

    @Transactional
    public CategoryModel saveCategory(CategoryModel categoryModel) {
        if(categoryRepository.existsByName(categoryModel.getName())) {
            throw new CategoryConflictException(categoryModel.getName());
        }

        return categoryRepository.save(categoryModel);
    }

    public Page<CategoryModel> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Optional<CategoryModel> findById(UUID id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public void delete(CategoryModel categoryModel) {
        categoryRepository.delete(categoryModel);
    }
}
