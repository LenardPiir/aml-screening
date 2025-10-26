package com.lhv.amlscreening.domain.util;

import com.lhv.amlscreening.domain.model.NoiseWord;
import java.util.Arrays;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NoiseWordUtil {

  public static boolean isNoiseWord(String word) {
    return Arrays.stream(NoiseWord.values())
        .anyMatch(noiseWord -> noiseWord.getWord().equalsIgnoreCase(word));
  }
}
