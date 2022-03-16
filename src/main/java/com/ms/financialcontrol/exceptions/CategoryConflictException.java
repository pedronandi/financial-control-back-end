package com.ms.financialcontrol.exceptions;

public class CategoryConflictException extends ConflictException {

    private static final long serialVersionUID = 1L;

    public CategoryConflictException(String name) {
        super(String.format("Name %s is already in use!", name));
    }
}