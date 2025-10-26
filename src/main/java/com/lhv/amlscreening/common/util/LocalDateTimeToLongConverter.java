package com.lhv.amlscreening.common.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.core.convert.converter.Converter;

public class LocalDateTimeToLongConverter implements Converter<LocalDateTime, Long> {

  @Override
  public Long convert(LocalDateTime source) {
    return source.toInstant(ZoneOffset.UTC).toEpochMilli();
  }
}
