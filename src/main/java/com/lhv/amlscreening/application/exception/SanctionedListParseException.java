package com.lhv.amlscreening.application.exception;

import com.lhv.amlscreening.application.model.SanctionedListParseErrorCode;
import lombok.Getter;

@Getter
public class SanctionedListParseException extends RuntimeException {

  private final SanctionedListParseErrorCode errorCode;

  public SanctionedListParseException(SanctionedListParseErrorCode errorCode, Object... args) {
    super(String.format(errorCode.getType(), args));
    this.errorCode = errorCode;
  }
}
