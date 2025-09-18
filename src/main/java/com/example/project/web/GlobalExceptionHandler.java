package com.example.project.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    // ---------- @Valid on @RequestBody DTOs ----------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest req) {

        List<ApiError.FieldValidationError> fields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> ApiError.FieldValidationError.builder()
                        .field(f.getField())
                        .message(f.getDefaultMessage())
                        .rejectedValue(f.getRejectedValue())
                        .build())
                .collect(Collectors.toList());

        return build(HttpStatus.BAD_REQUEST, "Validation failed", req, fields);
    }

    // ---------- @Validated on @RequestParam / @PathVariable ----------
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest req) {

        List<ApiError.FieldValidationError> fields = ex.getConstraintViolations()
                .stream()
                .map(v -> ApiError.FieldValidationError.builder()
                        .field(extractParamName(v))
                        .message(v.getMessage())
                        .rejectedValue(v.getInvalidValue())
                        .build())
                .collect(Collectors.toList());

        return build(HttpStatus.BAD_REQUEST, "Constraint violation", req, fields);
    }

    // ---------- Wrong types in query/path params ----------
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest req) {

        String hint = "";
        if (ex.getRequiredType() != null) {
            Class<?> t = ex.getRequiredType();
            if (t.equals(LocalDateTime.class)) {
                hint = " Use ISO-8601 like 2025-09-18T14:30:00.";
            } else if (t.equals(LocalDate.class)) {
                hint = " Use ISO date like 2025-09-18.";
            }
        }
        String msg = String.format("Parameter '%s' has invalid value '%s'. Expected type: %s.%s",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType() == null ? "unknown" : ex.getRequiredType().getSimpleName(),
                hint);

        return build(HttpStatus.BAD_REQUEST, msg, req, null);
    }

    // ---------- JSON parse / malformed body ----------
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Malformed JSON request", req, null);
    }

    // ---------- Missing required query parameter ----------
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpServletRequest req) {
        String msg = String.format("Missing required parameter '%s' of type %s",
                ex.getParameterName(), ex.getParameterType());
        return build(HttpStatus.BAD_REQUEST, msg, req, null);
    }

    // ---------- DB constraint violations (unique/email, FK, etc.) ----------
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "Data integrity violation", req, null);
    }

    // ---------- Security ----------
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "Access is denied", req, null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthentication(AuthenticationException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Authentication failed", req, null);
    }

    // ---------- Not found ----------
    @ExceptionHandler({ NoSuchElementException.class, jakarta.persistence.EntityNotFoundException.class })
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, messageOr("Resource not found", ex), req, null);
    }

    // ---------- Catch-all for RuntimeException from your services ----------
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex, HttpServletRequest req) {
        HttpStatus status = statusFromRuntimeMessage(ex.getMessage());
        return build(status, messageOr("Unexpected error", ex), req, null);
    }

    // ---------- Helpers ----------
    private ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest req,
                                           List<ApiError.FieldValidationError> fieldErrors) {
        ApiError body = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(req.getRequestURI())
                .fieldErrors(fieldErrors == null || fieldErrors.isEmpty() ? null : fieldErrors)
                .build();
        return ResponseEntity.status(status).body(body);
    }

    private String extractParamName(ConstraintViolation<?> v) {
        String path = v.getPropertyPath() == null ? "" : v.getPropertyPath().toString();
        int dot = path.lastIndexOf('.');
        return dot >= 0 ? path.substring(dot + 1) : path;
    }

    private String messageOr(String fallback, Exception ex) {
        return (ex.getMessage() == null || ex.getMessage().isBlank()) ? fallback : ex.getMessage();
    }

    /**
     * Maps your common RuntimeException messages to appropriate HTTP codes
     * without changing service code.
     */
    private HttpStatus statusFromRuntimeMessage(String msg) {
        if (msg == null) return HttpStatus.INTERNAL_SERVER_ERROR;
        String m = msg.toLowerCase(Locale.ROOT);
        if (m.contains("not found")) return HttpStatus.NOT_FOUND;          // e.g. "Patient not found"
        if (m.contains("already"))   return HttpStatus.CONFLICT;           // e.g. "already has an appointment"
        if (m.contains("invalid") || m.contains("bad") || m.contains("required"))
            return HttpStatus.BAD_REQUEST;
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
