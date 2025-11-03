package com.lhv.amlscreening.application.service;

import static com.lhv.amlscreening.testhelpers.fixtures.CommonTestFixtures.*;
import static org.mockito.Mockito.*;

import com.lhv.amlscreening.application.exception.SanctionedListParseException;
import com.lhv.amlscreening.application.model.SanctionedListParseErrorCode;
import com.lhv.amlscreening.domain.entity.SanctionedListEntity;
import com.lhv.amlscreening.domain.repository.sanctionedlist.elastic.SanctionedListElasticRepository;
import com.lhv.amlscreening.domain.repository.sanctionedlist.jpa.SanctionedListJPARepository;
import com.lhv.amlscreening.testhelpers.base.ABaseUnitTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
class SanctionedListServiceTest extends ABaseUnitTest {

  @Mock private SanctionedListParser sanctionedListParser;

  @Mock private SanctionedListJPARepository sanctionedListJPARepository;

  @Mock private SanctionedListElasticRepository sanctionedListElasticRepository;

  @InjectMocks private SanctionedListService sanctionedListService;

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    Resource testResource = new ClassPathResource(A_SANCTIONED_LIST_FILE_PATH);

    var filePathField =
        SanctionedListService.class.getDeclaredField(A_SANCTIONED_LIST_FILE_PATH_FIELD);
    filePathField.setAccessible(true);
    filePathField.set(sanctionedListService, testResource);
  }

  @Test
  void testReadSanctionListAndSave_Success() {
    List<SanctionedListEntity> entities = List.of(aSanctionedListEntity(A_FULL_NAME_JOHN));

    when(sanctionedListParser.parseCSV(any())).thenReturn(entities);

    sanctionedListService.readSanctionListAndSave();

    verify(sanctionedListJPARepository, times(1)).saveAll(entities);
    verify(sanctionedListElasticRepository, times(1)).saveAll(entities);
  }

  @Test
  void testReadSanctionListAndSave_Failure() {
    doThrow(new SanctionedListParseException(SanctionedListParseErrorCode.GENERIC_ERROR))
        .when(sanctionedListParser)
        .parseCSV(any());

    sanctionedListService.readSanctionListAndSave();

    verify(sanctionedListJPARepository, never()).saveAll(any());
    verify(sanctionedListElasticRepository, never()).saveAll(any());
  }

  @Test
  void testInit_CallsReadSanctionListAndSave() {
    when(sanctionedListParser.parseCSV(any())).thenReturn(List.of());

    sanctionedListService.init();

    verify(sanctionedListJPARepository, never()).saveAll(any());
    verify(sanctionedListElasticRepository, never()).saveAll(any());

    verify(sanctionedListParser, times(1)).parseCSV(any());
  }
}
