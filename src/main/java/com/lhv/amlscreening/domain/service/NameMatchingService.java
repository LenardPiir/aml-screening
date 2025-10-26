package com.lhv.amlscreening.domain.service;

import static com.lhv.amlscreening.domain.model.MatchConfidence.*;

import com.lhv.amlscreening.application.dto.SanctionedListMatchResponse;
import com.lhv.amlscreening.application.dto.SanctionedListNameResponse;
import com.lhv.amlscreening.application.exception.SanctionedListNameNotFoundException;
import com.lhv.amlscreening.application.mapper.SanctionedListMapper;
import com.lhv.amlscreening.domain.entity.SanctionedListEntity;
import com.lhv.amlscreening.domain.model.MatchConfidence;
import com.lhv.amlscreening.domain.repository.sanctionedlist.elastic.SanctionedListElasticRepository;
import com.lhv.amlscreening.domain.repository.sanctionedlist.jpa.SanctionedListJPARepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NameMatchingService {

  private final NameMatchingHelper nameMatchingHelper;
  private final SanctionedListElasticRepository sanctionedListElasticRepository;
  private final SanctionedListJPARepository sanctionedListJPARepository;
  private final SanctionedListMapper sanctionedListMapper;

  @Transactional
  public SanctionedListMatchResponse checkName(String fullName) {
    log.info("Checking name against watchlist: {}", fullName);

    String processedFullName = nameMatchingHelper.preprocessName(fullName);

    List<SanctionedListEntity> matchingNames =
        sanctionedListElasticRepository.findByFullNameFuzzy(processedFullName);

    if (matchingNames.isEmpty()) {
      log.info("No matches found for name: {}", fullName);
      return new SanctionedListMatchResponse(false, List.of(), NONE.name(), "No match found");
    }

    boolean isExactMatch =
        matchingNames.stream()
            .anyMatch(entity -> processedFullName.equalsIgnoreCase(entity.getFullName()));

    List<SanctionedListNameResponse> matchedNames =
        matchingNames.stream()
            .map(entity -> new SanctionedListNameResponse(entity.getId(), entity.getFullName()))
            .collect(Collectors.toList());

    MatchConfidence matchConfidence =
        nameMatchingHelper.determineConfidence(matchingNames, isExactMatch);
    String explanation =
        nameMatchingHelper.determineExplanation(matchingNames.size(), isExactMatch);

    log.info("Returning match response for name: {}", fullName);
    return new SanctionedListMatchResponse(true, matchedNames, matchConfidence.name(), explanation);
  }

  @Transactional
  public SanctionedListNameResponse addName(String fullName) {
    SanctionedListEntity entity = sanctionedListMapper.toSanctionedListEntity(fullName);

    sanctionedListJPARepository.save(entity);
    sanctionedListElasticRepository.save(entity);

    log.info("Added new watchlist name: {}", entity.getFullName());
    return new SanctionedListNameResponse(entity.getId(), entity.getFullName());
  }

  @Transactional
  public SanctionedListNameResponse updateName(UUID id, String fullName) {
    SanctionedListEntity entity =
        sanctionedListJPARepository
            .findById(id)
            .orElseThrow(() -> new SanctionedListNameNotFoundException(id));

    entity.setFullName(fullName);

    sanctionedListJPARepository.save(entity);
    sanctionedListElasticRepository.save(entity);

    log.info("Updated name for ID: {} with new name: {}", id, fullName);
    return new SanctionedListNameResponse(entity.getId(), entity.getFullName());
  }

  @Transactional
  public void deleteName(UUID id) {
    SanctionedListEntity entity =
        sanctionedListJPARepository
            .findById(id)
            .orElseThrow(() -> new SanctionedListNameNotFoundException(id));

    sanctionedListJPARepository.delete(entity);
    sanctionedListElasticRepository.delete(entity);

    log.info("Deleted name for ID: {}", id);
  }
}
