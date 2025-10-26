package com.lhv.amlscreening.domain.service;

import static com.lhv.amlscreening.testhelpers.fixtures.CommonTestFixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.lhv.amlscreening.application.dto.SanctionedListMatchResponse;
import com.lhv.amlscreening.application.dto.SanctionedListNameResponse;
import com.lhv.amlscreening.application.mapper.SanctionedListMapper;
import com.lhv.amlscreening.domain.entity.SanctionedListEntity;
import com.lhv.amlscreening.domain.model.MatchConfidence;
import com.lhv.amlscreening.domain.model.MatchExplanation;
import com.lhv.amlscreening.domain.repository.sanctionedlist.elastic.SanctionedListElasticRepository;
import com.lhv.amlscreening.domain.repository.sanctionedlist.jpa.SanctionedListJPARepository;
import com.lhv.amlscreening.testhelpers.base.ABaseUnitTest;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NameMatchingServiceTest extends ABaseUnitTest {

  @Mock private NameMatchingHelper nameMatchingHelper;

  @Mock private SanctionedListElasticRepository sanctionedListElasticRepository;

  @Mock private SanctionedListJPARepository sanctionedListJPARepository;

  @Mock private SanctionedListMapper sanctionedListMapper;

  @InjectMocks private NameMatchingService nameMatchingService;

  @Test
  void testCheckName_NoMatches() {
    String fullName = A_FULL_NAME_JOHN;
    String processedName = A_PROCESSED_NAME;

    when(nameMatchingHelper.preprocessName(fullName)).thenReturn(processedName);
    when(sanctionedListElasticRepository.findByFullNameFuzzy(processedName))
        .thenReturn(Collections.emptyList());

    SanctionedListMatchResponse response = nameMatchingService.checkName(fullName);

    assertFalse(response.isMatch());
    assertEquals(MatchExplanation.NO_MATCH.getExplanation(), response.explanation());
    assertEquals(MatchConfidence.NONE.name(), response.matchConfidence());
  }

  @Test
  void testCheckName_ExactMatch() {
    String fullName = A_FULL_NAME_JOHN;
    String processedName = A_PROCESSED_NAME;

    SanctionedListEntity entity = aSanctionedListEntity(fullName);

    when(nameMatchingHelper.preprocessName(fullName)).thenReturn(processedName);
    when(sanctionedListElasticRepository.findByFullNameFuzzy(processedName))
        .thenReturn(List.of(entity));
    when(nameMatchingHelper.determineConfidence(anyList(), eq(true)))
        .thenReturn(MatchConfidence.HIGH);
    when(nameMatchingHelper.determineExplanation(1, true))
        .thenReturn(MatchExplanation.EXACT_MATCH.getExplanation());

    SanctionedListMatchResponse response = nameMatchingService.checkName(fullName);

    assertTrue(response.isMatch());
    assertEquals(MatchConfidence.HIGH.name(), response.matchConfidence());
    assertEquals(MatchExplanation.EXACT_MATCH.getExplanation(), response.explanation());
  }

  @Test
  void testCheckName_FuzzyMatch() {
    String fullName = A_FULL_NAME_JOHN;
    String processedName = A_PROCESSED_NAME;
    SanctionedListEntity entity = aSanctionedListEntity(A_NAME_WITH_MISSING_LETTERS);

    when(nameMatchingHelper.preprocessName(fullName)).thenReturn(processedName);
    when(sanctionedListElasticRepository.findByFullNameFuzzy(processedName))
        .thenReturn(List.of(entity));
    when(nameMatchingHelper.determineConfidence(anyList(), eq(false)))
        .thenReturn(MatchConfidence.MEDIUM);
    when(nameMatchingHelper.determineExplanation(1, false))
        .thenReturn(MatchExplanation.SPELLING_VARIATION.getExplanation());

    SanctionedListMatchResponse response = nameMatchingService.checkName(fullName);

    assertTrue(response.isMatch());
    assertEquals(MatchConfidence.MEDIUM.name(), response.matchConfidence());
    assertEquals(MatchExplanation.SPELLING_VARIATION.getExplanation(), response.explanation());
  }

  @Test
  void testAddName() {
    String fullName = A_FULL_NAME_JOHN;
    String processedName = A_PROCESSED_NAME;
    SanctionedListEntity entity = aSanctionedListEntity(processedName);

    when(nameMatchingHelper.preprocessName(fullName)).thenReturn(processedName);
    when(sanctionedListMapper.toSanctionedListEntity(processedName)).thenReturn(entity);

    SanctionedListNameResponse response = nameMatchingService.addName(fullName);

    assertEquals(entity.getId(), response.id());
    assertEquals(processedName, response.fullName());
    verify(sanctionedListJPARepository, times(1)).save(entity);
    verify(sanctionedListElasticRepository, times(1)).save(entity);
  }
}
