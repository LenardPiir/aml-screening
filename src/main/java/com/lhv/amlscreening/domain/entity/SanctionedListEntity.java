package com.lhv.amlscreening.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Entity
// TODO: rename
@Table(name = "watchlist_names", schema = "aml_screening_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "sanctionedlistentity")
public class SanctionedListEntity {

  @Id private UUID id;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Transient
  @Field(type = FieldType.Date)
  private Long createdAtForElasticSearch;

  public SanctionedListEntity(UUID id, String fullName, LocalDateTime createdAt) {
    this.id = id;
    this.fullName = fullName;
    this.createdAt = createdAt;
    this.createdAtForElasticSearch = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli();
  }

  @PostLoad
  public void afterLoad() {
    if (createdAt != null) {
      this.createdAtForElasticSearch = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
  }
}
