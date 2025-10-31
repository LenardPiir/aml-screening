package com.lhv.amlscreening.application.service;

import com.lhv.amlscreening.application.exception.SanctionedListParseException;
import com.lhv.amlscreening.domain.entity.SanctionedListEntity;
import com.lhv.amlscreening.domain.repository.sanctionedlist.elastic.SanctionedListElasticRepository;
import com.lhv.amlscreening.domain.repository.sanctionedlist.jpa.SanctionedListJPARepository;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SanctionedListService {

  private final SanctionedListParser sanctionedListParser;
  private final SanctionedListJPARepository sanctionedListJPARepository;
  private final SanctionedListElasticRepository sanctionedListElasticRepository;

  @Value("${sanction.list.filepath}")
  private Resource sanctionedListFilePath;

  public void readSanctionListAndSave() {
    try (InputStream inputStream = sanctionedListFilePath.getInputStream()) {
      List<SanctionedListEntity> sanctionEntities = sanctionedListParser.parseCSV(inputStream);

      if (!sanctionEntities.isEmpty()) {
        sanctionedListJPARepository.saveAll(sanctionEntities);
        sanctionedListElasticRepository.saveAll(sanctionEntities);
      }

      log.info("Sanction list read from file and saved successfully.");
    } catch (SanctionedListParseException | IOException e) {
      log.error("Failed to parse the sanction list from the file", e);
    }
  }

  @PostConstruct
  public void init() {
    log.info("Initializing the sanction list read and save process at startup.");
    readSanctionListAndSave();
  }
}
