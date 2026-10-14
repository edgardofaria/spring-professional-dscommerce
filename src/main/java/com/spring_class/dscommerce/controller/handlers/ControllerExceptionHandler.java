package com.spring_class.dscommerce.controller.handlers;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.spring_class.dscommerce.dto.errors.ErrorResponse;
import com.spring_class.dscommerce.dto.errors.FieldError;
import com.spring_class.dscommerce.dto.errors.ValidationErrorResponse;
import com.spring_class.dscommerce.service.exceptions.DataBaseIntegrityViolationException;
import com.spring_class.dscommerce.service.exceptions.EmailAlreadyExistsException;
import com.spring_class.dscommerce.service.exceptions.OrderNotFoundException;
import com.spring_class.dscommerce.service.exceptions.ProductNotFoundException;
import com.spring_class.dscommerce.service.exceptions.UserNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, Exception e,
            HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFound(UserNotFoundException e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e, request);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> productNotFound(ProductNotFoundException e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e, request);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> orderNotFound(OrderNotFoundException e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e, request);
    }

    @ExceptionHandler(DataBaseIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> dataIntegrityViolation(DataBaseIntegrityViolationException e,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e, request);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> emailAlreadyExists(EmailAlreadyExistsException e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, e, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> methodArgumentNotValid(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        List<FieldError> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(f -> new FieldError(f.getField(), f.getDefaultMessage()))
                .toList();
        ValidationErrorResponse err = new ValidationErrorResponse(Instant.now(), status.value(), errors,
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFound(EntityNotFoundException e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> unauthorized(AuthenticationException e, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(Instant.now(), HttpStatus.UNAUTHORIZED.value(),
                "Missing, expired or invalid token", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDenied(AccessDeniedException e, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(Instant.now(), HttpStatus.FORBIDDEN.value(),
                "Access denied", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

}
