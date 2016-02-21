package com.pliesveld.flashnote.web.handler;

import com.pliesveld.flashnote.exception.ResourceRepositoryException;
import com.pliesveld.flashnote.web.dto.error.ErrorDetail;
import com.pliesveld.flashnote.web.dto.error.ValidationError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Maps business exceptions to meaningful error responses
 * Provides exception details for database integrity violations
 * Provides details for data validation errors
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceRepositoryException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceRepositoryException rnfe, HttpServletRequest request)
    {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        HttpStatus status = rnfe.getRepositoryStatus();
        errorDetail.setStatus(status.value());
        errorDetail.setTitle(rnfe.getRepositoryMessage());
        errorDetail.setDetail(rnfe.getMessage());
        errorDetail.setDeveloperMessage(rnfe.getClass().getSimpleName());
        return new ResponseEntity<>(errorDetail,null,status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handlePersistenceError(DataIntegrityViolationException dive, HttpServletRequest request)
    {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetail.setTitle("Database Integrity Violation");

        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        for(StackTraceElement stack : dive.getStackTrace())
        {
            sb.append(stack).append("\n");
            if(cnt++ > 3)
                break;
        }
        errorDetail.setDetail(dive.getMessage());
        errorDetail.setDeveloperMessage(sb.toString());
        return new ResponseEntity<>(errorDetail,null,HttpStatus.INTERNAL_SERVER_ERROR);
    }



    //   @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException manve, HttpServletRequest request)
    {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        String requestPath = (String) request.getAttribute("javax.servlet.error.request_uri");
        if(requestPath == null)
        {
            requestPath = request.getRequestURI();
        }
        errorDetail.setTitle("Validation Failed");
        errorDetail.setDetail("Input validation failed");
        errorDetail.setDeveloperMessage(manve.getClass().getSimpleName());

        List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();
        for(FieldError fe : fieldErrors)
        {
            List<ValidationError> validationErrorList = errorDetail.getErrors().get(fe.getField());

            if(validationErrorList == null)
            {
                validationErrorList = new ArrayList<ValidationError>();
                errorDetail.getErrors().put(fe.getField(),validationErrorList);
            }
            ValidationError validationError = new ValidationError();
            validationError.setCode(fe.getCode());
            validationError.setMessage(fe.getDefaultMessage());
            validationErrorList.add(validationError);

        }

        return new ResponseEntity<>(errorDetail,null,HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(status.value());
        errorDetail.setTitle("Message Not Readable");
        errorDetail.setDetail(ex.getClass().getName());
        errorDetail.setDeveloperMessage(ex.getMessage());
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());

        errorDetail.setTitle("Validation Failed");
        errorDetail.setDetail("Input validation failed");
        errorDetail.setDeveloperMessage(ex.getClass().getSimpleName());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for(FieldError fe : fieldErrors)
        {
            List<ValidationError> validationErrorList = errorDetail.getErrors().get(fe.getField());

            if(validationErrorList == null)
            {
                validationErrorList = new ArrayList<ValidationError>();
                errorDetail.getErrors().put(fe.getField(),validationErrorList);
            }
            ValidationError validationError = new ValidationError();
            validationError.setCode(fe.getCode());
            validationError.setMessage(fe.getDefaultMessage());
            validationErrorList.add(validationError);

        }
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }
}
