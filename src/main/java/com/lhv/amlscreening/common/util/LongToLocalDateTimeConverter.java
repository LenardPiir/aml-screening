package com.lhv.amlscreening.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.core.convert.converter.Converter;

public class LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {

  @Override
  public LocalDateTime convert(Long source) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(source), ZoneOffset.UTC);
  }
}
