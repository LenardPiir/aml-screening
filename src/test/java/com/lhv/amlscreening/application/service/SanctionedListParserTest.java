package com.lhv.amlscreening.application.service;

import static com.lhv.amlscreening.testhelpers.fixtures.CommonTestFixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lhv.amlscreening.application.exception.SanctionedListParseException;
import com.lhv.amlscreening.application.mapper.SanctionedListMapper;
import com.lhv.amlscreening.application.model.SanctionedListParseErrorCode;
import com.lhv.amlscreening.domain.entity.SanctionedListEntity;
import com.lhv.amlscreening.testhelpers.base.ABaseUnitTest;
import com.opencsv.CSVReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SanctionedListParserTest extends ABaseUnitTest {

  @Mock private SanctionedListMapper sanctionedListMapper;

  @InjectMocks private SanctionedListParser sanctionedListParser;

  private static Stream<Arguments> errorCases_provider() {
    return Stream.of(
        Arguments.of("Empty CSV", "", "CSV file is empty or missing header row"),
        Arguments.of(
            "Missing Naal_wholename column",
            "SomeOtherColumn\nJohn Doe",
            "Required column 'Naal_wholename' missing in the CSV file"));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("errorCases_provider")
  void testParseCSV_ErrorCases(String caseName, String csvData, String expectedMessage) {
    InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());

    SanctionedListParseException thrown =
        assertThrows(
            SanctionedListParseException.class,
            () -> {
              sanctionedListParser.parseCSV(inputStream);
            });

    assertEquals(expectedMessage, thrown.getMessage());
  }

  @Test
  void testParseCSV_IOException() throws Exception {
    InputStream mockInputStream = mock(InputStream.class);
    CSVReader mockReader = mock(CSVReader.class);
    when(mockReader.readNext()).thenThrow(new IOException());

    // TODO: refactor
    SanctionedListParser sanctionedListSpyParser = spy(sanctionedListParser);
    doReturn(mockReader).when(sanctionedListSpyParser).createCSVReader(any(InputStream.class));

    SanctionedListParseException thrown =
        assertThrows(
            SanctionedListParseException.class,
            () -> sanctionedListSpyParser.parseCSV(mockInputStream));

    assertEquals(SanctionedListParseErrorCode.GENERIC_ERROR.getType(), thrown.getMessage());
  }

  @Test
  void testParseCSV_Success() {
    when(sanctionedListMapper.toSanctionedListEntity(A_FULL_NAME_JOHN))
        .thenReturn(aSanctionedListEntity(A_FULL_NAME_JOHN));
    when(sanctionedListMapper.toSanctionedListEntity(A_FULL_NAME_JANE))
        .thenReturn(aSanctionedListEntity(A_FULL_NAME_JANE));

    InputStream inputStream = new ByteArrayInputStream(A_CSV_DATA.getBytes());

    List<SanctionedListEntity> result = sanctionedListParser.parseCSV(inputStream);

    assertNotNull(result);
    assertEquals(2, result.size());

    verify(sanctionedListMapper, times(2)).toSanctionedListEntity(anyString());
  }
}
