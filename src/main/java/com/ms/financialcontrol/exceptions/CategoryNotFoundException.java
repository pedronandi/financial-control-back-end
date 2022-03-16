package com.ms.financialcontrol.exceptions;

import java.util.UUID;

public class CategoryNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public CategoryNotFoundException(UUID id) {
        super(String.format("Category id %s not found!", id));
    }
}
