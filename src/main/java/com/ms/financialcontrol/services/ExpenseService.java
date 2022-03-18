package com.ms.financialcontrol.services;

import com.ms.financialcontrol.exceptions.CategoryNotFoundException;
import com.ms.financialcontrol.models.ExpenseModel;
import com.ms.financialcontrol.repositories.ExpenseRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;

    public boolean existsById(UUID id) { return expenseRepository.existsById(id); }

    @Transactional
    public Object saveExpense(ExpenseModel expenseModel) {
        if(!categoryService.existsById(expenseModel.getCategory().getId())) {
            throw new CategoryNotFoundException(expenseModel.getCategory().getId());
        }

        return expenseRepository.save(expenseModel);
    }

    public Page<ExpenseModel> findAll(Pageable pageable) {
        return expenseRepository.findAll(pageable);
    }

    public Optional<ExpenseModel> findById(UUID id) {
        return expenseRepository.findById(id);
    }

    @Transactional
    public void delete(ExpenseModel expenseModel) {
        expenseRepository.delete(expenseModel);
    }
}
