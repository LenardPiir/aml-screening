package com.lhv.amlscreening.application.service;

import com.lhv.amlscreening.application.exception.SanctionedListParseException;
import com.lhv.amlscreening.application.mapper.SanctionedListMapper;
import com.lhv.amlscreening.application.model.SanctionedListParseErrorCode;
import com.lhv.amlscreening.domain.entity.SanctionedListEntity;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SanctionedListParser {

  private final SanctionedListMapper sanctionedListMapper;

  private static final String NAME_COLUMN = "Naal_wholename";

  public List<SanctionedListEntity> parseCSV(InputStream inputStream) {
    try (CSVReader reader = createCSVReader(inputStream)) {
      return processCSV(reader);
    } catch (IOException | CsvValidationException e) {
      log.error("Failed to parse the CSV file", e);
      throw new SanctionedListParseException(SanctionedListParseErrorCode.GENERIC_ERROR);
    }
  }

  CSVReader createCSVReader(InputStream inputStream) {
    return new CSVReaderBuilder(new InputStreamReader(inputStream))
        .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
        .build();
  }

  List<SanctionedListEntity> processCSV(CSVReader reader)
      throws IOException, CsvValidationException {
    log.info("Processing CSV data...");

    String[] header = reader.readNext();
    validateHeader(header);

    Map<String, Integer> headerMap = buildHeaderMap(header);
    List<SanctionedListEntity> sanctionedEntities = new ArrayList<>();

    String[] line;
    while ((line = reader.readNext()) != null) {
      String fullName = extractFullName(headerMap, line);
      sanctionedEntities.add(sanctionedListMapper.toSanctionedListEntity(fullName));
    }

    log.info("Processed {} entries from the CSV file.", sanctionedEntities.size());
    return sanctionedEntities;
  }

  void validateHeader(String[] header) {
    if (header == null || header.length == 0) {
      log.error("CSV file is empty or missing header row.");
      throw new SanctionedListParseException(SanctionedListParseErrorCode.FILE_EMPTY);
    }
  }

  Map<String, Integer> buildHeaderMap(String[] header) {
    Map<String, Integer> headerMap = new HashMap<>();
    for (int i = 0; i < header.length; i++) {
      headerMap.put(header[i].trim(), i);
    }
    return headerMap;
  }

  String extractFullName(Map<String, Integer> headerMap, String[] line) {
    Integer columnIndex = headerMap.get(NAME_COLUMN);
    if (columnIndex == null) {
      log.error("Column '{}' not found in CSV header.", NAME_COLUMN);
      throw new SanctionedListParseException(
          SanctionedListParseErrorCode.MISSING_COLUMN, NAME_COLUMN);
    }
    return line[columnIndex];
  }
}
