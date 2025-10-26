package com.lhv.amlscreening.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoiseWord {
  THE("the"),
  TO("to"),
  AN("an"),
  MRS("mrs"),
  MR("mr"),
  AND("and");

  private final String word;
}
