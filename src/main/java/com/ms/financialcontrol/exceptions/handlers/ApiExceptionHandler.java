package com.ms.financialcontrol.exceptions.handlers;

import com.ms.financialcontrol.exceptions.ConflictException;
import com.ms.financialcontrol.exceptions.EntityNotFoundException;
import com.sun.istack.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();
    private static final List<Problem.Object> PROBLEM_OBJECT = null;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders httpHeaders, HttpStatus httpStatus, WebRequest webRequest) {

        ProblemType problemType = ProblemType.INVALID_DATA;
        String detail = "There is invalid data. Fill the data correctly and try again";
        List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
        List<Problem.Object> problemObjects = allErrors
                .stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }

                    return Problem.Object.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());

        return handleException(httpStatus, problemType, detail, exception, webRequest, httpHeaders, problemObjects);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest webRequest) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.SYSTEM_ERROR;
        String detail = "An internal and unexpected error has occurred";

        return handleException(status, problemType, detail, exception, webRequest, HTTP_HEADERS, PROBLEM_OBJECT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException exception, WebRequest webRequest) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;
        String detail = exception.getMessage();

        return handleException(status, problemType, detail, exception, webRequest, HTTP_HEADERS, PROBLEM_OBJECT);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handleConflict(ConflictException exception, WebRequest webRequest) {
        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.CONFLICT_DATA;
        String detail = exception.getMessage();

        return handleException(status, problemType, detail, exception, webRequest, HTTP_HEADERS, PROBLEM_OBJECT);
    }

    private ResponseEntity<Object> handleException(HttpStatus httpStatus,
                                                   ProblemType problemType,
                                                   String detail,
                                                   Exception exception,
                                                   WebRequest webRequest,
                                                   HttpHeaders httpHeaders,
                                                   @Nullable List<Problem.Object> problemObjects) {

        Problem problem = Problem.builder()
                .timestamp(OffsetDateTime.now())
                .status(httpStatus.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail)
                .userMessage(detail)
                .objects(problemObjects)
                .build();

        log.error(exception.getMessage(), exception);

        return handleExceptionInternal(exception, problem, httpHeaders, httpStatus, webRequest);
    }
}
