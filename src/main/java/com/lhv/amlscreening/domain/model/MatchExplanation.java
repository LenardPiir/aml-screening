package com.lhv.amlscreening.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchExplanation {
  NO_MATCH("No match found"),
  EXACT_MATCH("Exact match found"),
  PARTIAL_MATCH("Partial match detected"),
  SPELLING_VARIATION("Spelling variation detected"),
  ABBREVIATION_MATCH("Abbreviation match detected"),
  GENERIC_MATCH("Multiple suspicious matches found, further review recommended");

  private final String explanation;
}
