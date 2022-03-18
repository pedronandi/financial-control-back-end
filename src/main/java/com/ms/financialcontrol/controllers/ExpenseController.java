package com.ms.financialcontrol.controllers;

import com.ms.financialcontrol.dtos.ExpenseDto;
import com.ms.financialcontrol.exceptions.ExpenseNotFoundException;
import com.ms.financialcontrol.mappers.ExpenseMapper;
import com.ms.financialcontrol.models.ExpenseModel;
import com.ms.financialcontrol.services.ExpenseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@AllArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseMapper expenseMapper;
    private static final String EXPENSE_FOUND = "expense {} found";

    @PostMapping
    public ResponseEntity<Object> saveExpense(@RequestBody @Valid ExpenseDto expenseDto) {
        ExpenseModel expenseModel = expenseMapper.toModel(expenseDto);
        log.debug("expenseModel mapped");

        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.saveExpense(expenseModel));
    }

    @GetMapping
    public ResponseEntity<Page<ExpenseModel>> getAllExpenses(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getExpenseById(@PathVariable(value = "id") UUID id) {
        ExpenseModel expenseRequest = expenseService.findById(id).orElseThrow(() -> new ExpenseNotFoundException(id));
        log.debug(EXPENSE_FOUND, expenseRequest.getId());

        return ResponseEntity.status(HttpStatus.OK).body(expenseRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateExpense(@PathVariable(value = "id") UUID id, @RequestBody @Valid ExpenseDto expenseDto) {
        if(!expenseService.existsById(id)) {
            throw new ExpenseNotFoundException(id);
        }

        log.debug(EXPENSE_FOUND, id);

        ExpenseModel expenseModel = expenseMapper.toModel(expenseDto);
        log.debug("expenseModel mapped");

        expenseModel.setId(id);

        return ResponseEntity.status(HttpStatus.OK).body(expenseService.saveExpense(expenseModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteExpense(@PathVariable(value = "id") UUID id) {
        ExpenseModel expenseModel = expenseService.findById(id).orElseThrow(() -> new ExpenseNotFoundException(id));
        log.debug(EXPENSE_FOUND, expenseModel.getId());

        expenseService.delete(expenseModel);

        return ResponseEntity.status(HttpStatus.OK).body(String.format("Expense id %s deleted successfully!", id));
    }
}
