package com.lhv.amlscreening.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SanctionedListParseErrorCode {
  FILE_EMPTY("CSV file is empty or missing header row"),
  MISSING_COLUMN("Required column '%s' missing in the CSV file"),
  INVALID_FORMAT("Invalid format in the CSV file at line %d"),
  GENERIC_ERROR("An error occurred while parsing the CSV file");

  private final String type;
}
