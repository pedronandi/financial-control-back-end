package com.ms.financialcontrol.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
@AllArgsConstructor
public class ExceptionResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Date timestamp;
    private final String message;
    private final String details;
}
