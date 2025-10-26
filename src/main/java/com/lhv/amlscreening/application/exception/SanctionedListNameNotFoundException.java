package com.lhv.amlscreening.application.exception;

import java.util.UUID;

public class SanctionedListNameNotFoundException extends RuntimeException {

  public SanctionedListNameNotFoundException(UUID id) {
    super("Sanctioned name not found for ID: " + id);
  }
}
