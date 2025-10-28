package com.lhv.amlscreening.testhelpers.fixtures;

import com.lhv.amlscreening.domain.entity.SanctionedListEntity;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonTestFixtures {

  public static final String A_FULL_NAME_JOHN = "John Doe";
  public static final String A_FULL_NAME_JANE = "Jane Smith";
  public static final String A_FULL_NAME_SADDAM = "Saddam Hussein";
  public static final String A_FULL_NAME_SADDAM_AL_TIKRITI = "Saddam Hussein Al-Tikriti";
  public static final String A_PROCESSED_NAME = "john doe";
  public static final String A_NAME_WITH_MISSING_LETTERS = "Jon Doe";

  public static final UUID A_UUID = UUID.randomUUID();

  public static final String A_SANCTIONED_LIST_FILE_PATH =
      "src/test/resources/dataset/20251007-FULL-1_0.csv";
  public static final String A_SANCTIONED_LIST_FILE_PATH_FIELD = "sanctionedListFilePath";

  public static final String A_CSV_DATA = "Naal_wholename\nJohn Doe\nJane Smith";

  public SanctionedListEntity aSanctionedListEntity(String fullName) {
    return new SanctionedListEntity(UUID.randomUUID(), fullName, System.currentTimeMillis());
  }
}
