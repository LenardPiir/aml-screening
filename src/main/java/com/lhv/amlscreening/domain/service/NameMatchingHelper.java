package com.lhv.amlscreening.domain.service;

import static com.lhv.amlscreening.domain.model.MatchConfidence.*;
import static com.lhv.amlscreening.domain.model.MatchExplanation.*;

import com.lhv.amlscreening.domain.entity.SanctionedListEntity;
import com.lhv.amlscreening.domain.model.MatchConfidence;
import com.lhv.amlscreening.domain.util.NoiseWordUtil;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NameMatchingHelper {

  public String preprocessName(String name) {
    return Stream.of(name.toLowerCase().split(" "))
        .filter(word -> !NoiseWordUtil.isNoiseWord(word))
        .collect(Collectors.joining(" "))
        .replaceAll("\\s+", " ")
        .trim();
  }

  public MatchConfidence determineConfidence(
      List<SanctionedListEntity> matchingNames, boolean isExactMatch) {
    if (isExactMatch) {
      return HIGH;
    }

    return switch (matchingNames.size()) {
      case 0 -> NONE;
      case 1, 2, 3, 4, 5 -> MEDIUM;
      default -> HIGH;
    };
  }

  public String determineExplanation(int numMatches, boolean isExactMatch) {
    if (isExactMatch) {
      return EXACT_MATCH.getExplanation();
    }

    return switch (numMatches) {
      case 0 -> NO_MATCH.getExplanation();
      case 1, 2, 3 -> PARTIAL_MATCH.getExplanation();
      case 4, 5 -> SPELLING_VARIATION.getExplanation();
      case 6, 7 -> ABBREVIATION_MATCH.getExplanation();
      default -> GENERIC_MATCH.getExplanation();
    };
  }
}
