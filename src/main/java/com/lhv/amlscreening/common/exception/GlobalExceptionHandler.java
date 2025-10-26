package com.lhv.amlscreening.common.exception;

import com.lhv.amlscreening.application.exception.SanctionedListParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(SanctionedListParseException.class)
  public ResponseEntity<String> handleSanctionedListParseException(
      SanctionedListParseException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error parsing the sanctioned list: " + ex.getMessage());
  }
}
