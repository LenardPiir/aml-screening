package com.lhv.amlscreening.common.exception;

import com.lhv.amlscreening.application.exception.SanctionedListNameNotFoundException;
import com.lhv.amlscreening.application.exception.SanctionedListParseException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(SanctionedListParseException.class)
  public ResponseEntity<String> handleSanctionedListParseException(
      SanctionedListParseException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error parsing the sanctioned list: " + ex.getMessage());
  }

  @ExceptionHandler(SanctionedListNameNotFoundException.class)
  public ResponseEntity<String> handleSanctionedListNameNotFoundException(
      SanctionedListNameNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex, WebRequest request) {

    Map<String, String> fieldErrors =
        ex.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.toMap(
                    FieldError::getField,
                    e -> Objects.requireNonNullElse(e.getDefaultMessage(), "Invalid value")));

    ValidationErrorResponse response =
        new ValidationErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "Validation failed",
            request.getDescription(false).replace("uri=", ""),
            fieldErrors);

    return ResponseEntity.badRequest().body(response);
  }
}
