package com.lhv.amlscreening.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchConfidence {
  NONE("none"),
  MEDIUM("medium"),
  HIGH("high");

  private final String label;
}
