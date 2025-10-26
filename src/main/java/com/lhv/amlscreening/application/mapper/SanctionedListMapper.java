package com.lhv.amlscreening.application.mapper;

import com.lhv.amlscreening.domain.entity.SanctionedListEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SanctionedListMapper {

  public SanctionedListEntity toSanctionedListEntity(String fullName) {
    return SanctionedListEntity.builder()
        .id(UUID.randomUUID())
        .fullName(fullName)
        .createdAt(LocalDateTime.now())
        .build();
  }
}
