package com.example.FormSystem.exception;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, String> validationMap = new HashMap<>();
        String errorMessage = MessageConstant.VALIDATION_FAILED;

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationMap.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> errors = new HashMap<>();
        errors.put("validation", validationMap);
        errors.put("code", ex.getStatusCode());
        errors.put("message", errorMessage);

        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
                                                                                   HttpServletRequest request) {

        Map<String, Object> errors = Map.of(
                "message", ex.getMessage(),
                "code", String.valueOf(HttpStatus.BAD_REQUEST.value()));

        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex,
            HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMessage(),
                "code", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMostSpecificCause().getMessage(),
                "code", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex,
            HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMessage(),
                "code", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex,
            HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMessage(),
                "code", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex,
            HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMessage(),
                "code", String.valueOf(HttpStatus.FORBIDDEN.value()));
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMessage(),
                "code", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ SignatureException.class, MalformedJwtException.class })
    public ResponseEntity<ErrorResponse> handleInvalidJwtException(Exception ex, HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMessage(),
                "code", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex,
            HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMessage(),
                "code", String.valueOf(HttpStatus.NOT_FOUND.value()));
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex,
            HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMessage(),
                "code", String.valueOf(HttpStatus.NOT_FOUND.value()));
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
            HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMostSpecificCause().getMessage(),
                "code", String.valueOf(HttpStatus.CONFLICT.value()));
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMessage(),
                "code", String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()));
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex,
            HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMessage(),
                "code", String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()));
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex,
            HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMessage(),
                "code", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        Map<String, Object> errors = Map.of(
                "message", ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred",
                "code", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "exception", ex.getClass().getName());
        ErrorResponse errorResponse = new ErrorResponse(errors, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}