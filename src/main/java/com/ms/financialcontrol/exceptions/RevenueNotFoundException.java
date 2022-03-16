package com.ms.financialcontrol.exceptions;

import java.util.UUID;

public class RevenueNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public RevenueNotFoundException(UUID id) {
        super(String.format("Revenue id %s not found!", id));
    }
}
