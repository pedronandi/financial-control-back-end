package com.ms.financialcontrol.exceptions;

import java.util.UUID;

public class ExpenseNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public ExpenseNotFoundException(UUID id) {
        super(String.format("Expense id %s not found!", id));
    }
}
