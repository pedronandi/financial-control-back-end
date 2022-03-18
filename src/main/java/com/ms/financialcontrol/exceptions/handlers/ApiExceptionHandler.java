package com.ms.financialcontrol.exceptions.handlers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.ms.financialcontrol.exceptions.ConflictException;
import com.ms.financialcontrol.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
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
        List<Problem.Object> problemObjects = getProblemObjectsList(allErrors);

        return handleException(httpStatus, problemType, detail, exception, webRequest, httpHeaders, problemObjects);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                                  HttpHeaders httpHeaders, HttpStatus httpStatus, WebRequest webRequest) {

        Throwable rootCause = ExceptionUtils.getRootCause(exception);
        ProblemType problemType = ProblemType.UNREADABLE_MESSAGE;
        String detail = "Request body is invalid. Please, check the syntax error";

        if(rootCause instanceof DateTimeParseException) {
            return handleInvalidDateFormat((DateTimeParseException) rootCause, problemType, httpHeaders, httpStatus, webRequest);
        } else if(rootCause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) rootCause, problemType, httpHeaders, httpStatus, webRequest);
        } else if(rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause, problemType, httpHeaders, httpStatus, webRequest);
        }

        return handleException(httpStatus, problemType, detail, exception, webRequest, httpHeaders, PROBLEM_OBJECT);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception, HttpHeaders httpHeaders,
                                                        HttpStatus httpStatus, WebRequest webRequest) {

        if (exception instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) exception, httpHeaders, httpStatus, webRequest);
        }

        return super.handleTypeMismatch(exception, httpHeaders, httpStatus, webRequest);
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

    private List<Problem.Object> getProblemObjectsList(List<ObjectError> allErrors) {
        return allErrors
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
    }

    private ResponseEntity<Object> handleInvalidDateFormat(DateTimeParseException exception, ProblemType problemType,
                                                       HttpHeaders httpHeaders, HttpStatus httpStatus, WebRequest webRequest) {

        String detail = String.format("'%s' is not compatible with UTC format", exception.getParsedString());

        return handleException(httpStatus, problemType, detail, exception, webRequest, httpHeaders, PROBLEM_OBJECT);
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException exception, ProblemType problemType,
                                                       HttpHeaders httpHeaders, HttpStatus httpStatus, WebRequest webRequest) {

        String path = joinPath(exception.getPath());
        StringBuilder detail = new StringBuilder(String.format("Property '%s' is receiving invalid data '%s'. Inform valid data to %s",
                path, exception.getValue(), exception.getTargetType().getSimpleName()));
        Object[] enumConstants = exception.getTargetType().getEnumConstants();

        if(enumConstants != null) {
            detail.append(getMessageComplementWhenEnumInvalidFormat(enumConstants));
        }

        return handleException(httpStatus, problemType, detail.toString(), exception, webRequest, httpHeaders, PROBLEM_OBJECT);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException exception, ProblemType problemType,
                                                         HttpHeaders httpHeaders, HttpStatus httpStatus, WebRequest webRequest) {

        String path = joinPath(exception.getPath());
        String detail = String.format("Property '%s' does not exists. Adjust or remove this property and try again", path);

        return handleException(httpStatus, problemType, detail, exception, webRequest, httpHeaders, PROBLEM_OBJECT);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

    private String getMessageComplementWhenEnumInvalidFormat(Object[] enumConstants) {
        return String.format(": %s", Arrays
                .stream(enumConstants)
                .map(Object::toString)
                .collect(Collectors.toList())
                .toString());
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception, HttpHeaders httpHeaders,
            HttpStatus httpStatus, WebRequest webRequest) {

        ProblemType problemType = ProblemType.INVALID_PARAM;
        String detail = String.format("URL param '%s' is receiving invalid data '%s'. Inform valid data to %s",
                exception.getName(), exception.getValue(), exception.getRequiredType().getSimpleName());

        return handleException(httpStatus, problemType, detail, exception, webRequest, httpHeaders, PROBLEM_OBJECT);
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
