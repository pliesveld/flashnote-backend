package com.pliesveld.flashnote.web.handler;

import com.pliesveld.flashnote.exception.ResourceRepositoryException;
import com.pliesveld.flashnote.logging.Markers;
import com.pliesveld.flashnote.web.dto.error.ConstraintErrorDetail;
import com.pliesveld.flashnote.web.dto.error.ErrorDetail;
import com.pliesveld.flashnote.web.dto.error.ValidationError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.pliesveld.flashnote.logging.Markers.REST_EXCEPTION;

/**
 * Maps business exceptions to meaningful error responses
 * Provides exception details for database integrity violations
 * Provides details for data validation errors
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LogManager.getLogger();

    @ExceptionHandler(ResourceRepositoryException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceRepositoryException rnfe, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(Instant.now().toEpochMilli());
        HttpStatus status = rnfe.getRepositoryStatus();
        errorDetail.setStatus(status.value());
        errorDetail.setTitle(rnfe.getRepositoryMessage());
        errorDetail.setDetail(rnfe.getMessage());
        errorDetail.setDeveloperMessage(rnfe.getClass().getSimpleName());
        return new ResponseEntity<>(errorDetail, null, status);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handlePersistenceError(DataIntegrityViolationException dive, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(Instant.now().toEpochMilli());
        errorDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetail.setTitle("Database Integrity Violation");

        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        for (StackTraceElement stack : dive.getStackTrace()) {
            sb.append(stack).append("\n");
            if (cnt++ > 3)
                break;
        }
        errorDetail.setDetail(dive.getMessage());
        errorDetail.setDeveloperMessage(sb.toString());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(Instant.now().toEpochMilli());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());

        errorDetail.setTitle("Request parameters");
        errorDetail.setDetail("Parameter validation failed: " + ex.getParameterName() + " type: " + ex.getParameterType());

        request.getHeaderNames().forEachRemaining((s) -> errorDetail.getHeaders().put(s, request.getHeader(s)));

        errorDetail.setDeveloperMessage(ex.getMessage());

        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(Instant.now().toEpochMilli());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setDetail(ex.getMessage());

        if (ex instanceof MethodArgumentConversionNotSupportedException) {
            MethodArgumentConversionNotSupportedException macnse = (MethodArgumentConversionNotSupportedException) ex;
            errorDetail.setDeveloperMessage("name: " + macnse.getName() + " parameter: " + macnse.getParameter().getContainingClass().getSimpleName());
        }

        LOG.debug(REST_EXCEPTION, "handleConversionNotSupported");
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipartError(MultipartException me, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(Instant.now().toEpochMilli());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        String requestPath = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestPath == null) {
            requestPath = request.getRequestURI();
        }
        errorDetail.setTitle("Multipart");
        errorDetail.setDetail("Multipart validation failure");
        errorDetail.setDeveloperMessage(me.getClass().getSimpleName());

        if (me instanceof MaxUploadSizeExceededException) {
            MaxUploadSizeExceededException musee = (MaxUploadSizeExceededException) me;
            errorDetail.setDeveloperMessage(musee.getClass().getSimpleName());
            errorDetail.setDetail("Upload exceeded " + musee.getMaxUploadSize());
        }

        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException e) {
        ConstraintErrorDetail errorDetail = new ConstraintErrorDetail();
        errorDetail.setTimestamp(Instant.now().toEpochMilli());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());

        List<ValidationError> validationErrors = errorDetail.getErrors();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            //LOG.debug(REST_EXCEPTION, "violation -> " + violation);
            ValidationError error = new ValidationError();
            error.setCode(violation.getRootBeanClass().getSimpleName());
            error.setMessage(violation.getMessage());
            validationErrors.add(error);

        }
        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(Instant.now().toEpochMilli());
        errorDetail.setStatus(status.value());
        errorDetail.setTitle("Message Not Readable");
        errorDetail.setDetail(ex.getClass().getName());
        errorDetail.setDeveloperMessage(ex.getMessage());
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.debug(REST_EXCEPTION, "handleMethodArgumentNotValid {}", ex.getMessage());

        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(Instant.now().toEpochMilli());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());

        errorDetail.setTitle("Validation Failed");
        errorDetail.setDetail("Input validation failed");
        errorDetail.setDeveloperMessage(ex.getClass().getSimpleName());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fe : fieldErrors) {
            List<ValidationError> validationErrorList = errorDetail.getErrors().get(fe.getField());

            if (validationErrorList == null) {
                validationErrorList = new ArrayList<ValidationError>();
                errorDetail.getErrors().put(fe.getField(), validationErrorList);
            }
            ValidationError validationError = new ValidationError();
            validationError.setCode(fe.getCode());
            validationError.setMessage(fe.getDefaultMessage());
            validationErrorList.add(validationError);

        }
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.debug(Markers.REST_EXCEPTION_INTERNAL, ex.getMessage());
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.debug(REST_EXCEPTION, "handleNoSuchRequestHanldingMethod");
        return super.handleNoSuchRequestHandlingMethod(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.debug(REST_EXCEPTION, "handleHttpRequestMethodNotSupported");
        return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.debug(REST_EXCEPTION, "handleHttpMediaTypeNotSupported", ex);
        return super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.debug(REST_EXCEPTION, "handleHttpMediaTypeNotAcceptable");
        return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.debug(REST_EXCEPTION, "handleMissingPathVariable");
        return super.handleMissingPathVariable(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.debug(REST_EXCEPTION, "handleServletRequestBindingException");
        return super.handleServletRequestBindingException(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.debug(REST_EXCEPTION, "handleTypeMismatch");
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.debug(REST_EXCEPTION, "handleHttpMessageNotWritable");
        return super.handleHttpMessageNotWritable(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.debug(REST_EXCEPTION, "handleMissingServletRequestPart");
        return super.handleMissingServletRequestPart(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.debug(REST_EXCEPTION, "handleBindException");
        return super.handleBindException(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.debug(REST_EXCEPTION, "handleNoHandlerFoundException");
        return super.handleNoHandlerFoundException(ex, headers, status, request);
    }
}
