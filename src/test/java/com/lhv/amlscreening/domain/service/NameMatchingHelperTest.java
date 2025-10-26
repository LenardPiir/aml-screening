package com.lhv.amlscreening.domain.service;

import static com.lhv.amlscreening.domain.model.MatchConfidence.*;
import static com.lhv.amlscreening.domain.model.MatchExplanation.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.lhv.amlscreening.domain.entity.SanctionedListEntity;
import com.lhv.amlscreening.domain.model.MatchConfidence;
import com.lhv.amlscreening.testhelpers.base.ABaseUnitTest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NameMatchingHelperTest extends ABaseUnitTest {

  private final NameMatchingHelper nameMatchingHelper = new NameMatchingHelper();

  static Stream<Arguments> confidenceTestCases_provider() {
    return Stream.of(
        Arguments.of("Exact match", 1, true, HIGH),
        Arguments.of("Non-exact match with 0 matches", 0, false, NONE),
        Arguments.of("Non-exact match with 1 match", 1, false, MEDIUM),
        Arguments.of("Non-exact match with 5 matches", 5, false, MEDIUM),
        Arguments.of("Non-exact match with 6 matches", 6, false, HIGH));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("confidenceTestCases_provider")
  void testDetermineConfidence(
      String caseName, int numMatches, boolean isExactMatch, MatchConfidence expectedConfidence) {
    List<SanctionedListEntity> matchingNames =
        Stream.generate(SanctionedListEntity::new).limit(numMatches).collect(Collectors.toList());

    MatchConfidence result = nameMatchingHelper.determineConfidence(matchingNames, isExactMatch);
    assertEquals(expectedConfidence, result);
  }

  static Stream<Arguments> explanationTestCases_provider() {
    return Stream.of(
        Arguments.of("Exact match", 0, true, EXACT_MATCH.getExplanation()),
        Arguments.of("Non-exact match with 0 matches", 0, false, NO_MATCH.getExplanation()),
        Arguments.of("Non-exact match with 1 match", 1, false, PARTIAL_MATCH.getExplanation()),
        Arguments.of(
            "Non-exact match with 5 matches", 5, false, SPELLING_VARIATION.getExplanation()),
        Arguments.of(
            "Non-exact match with 6 matches", 6, false, ABBREVIATION_MATCH.getExplanation()),
        Arguments.of(
            "Non-exact match with more than 7 matches", 8, false, GENERIC_MATCH.getExplanation()));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("explanationTestCases_provider")
  void testDetermineExplanation(
      String caseName, int numMatches, boolean isExactMatch, String expectedExplanation) {
    String result = nameMatchingHelper.determineExplanation(numMatches, isExactMatch);
    assertEquals(expectedExplanation, result);
  }

  static Stream<Arguments> preprocessNameTestCases_provider() {
    return Stream.of(
        Arguments.of("Empty string", "", ""),
        Arguments.of("String with noise words", "The quick brown fox", "quick brown fox"),
        Arguments.of("String with mixed case", "John DOE", "john doe"),
        Arguments.of("String with extra spaces", "  John  Doe  ", "john doe"),
        Arguments.of("String without noise words", "Alice Bob", "alice bob"));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("preprocessNameTestCases_provider")
  void testPreprocessName(String caseName, String input, String expected) {
    String result = nameMatchingHelper.preprocessName(input);
    assertEquals(expected, result);
  }
}
