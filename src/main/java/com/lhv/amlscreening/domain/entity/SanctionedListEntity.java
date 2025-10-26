package com.lhv.amlscreening.domain.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Entity
@Table(name = "sanctioned_list_names", schema = "aml_screening_schema")
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
  @Field(type = FieldType.Date)
  private Long createdAt;

  @Column(name = "updated_at")
  @Field(type = FieldType.Date)
  private Long updatedAt;

  public SanctionedListEntity(UUID id, String fullName, Long createdAt) {
    this.id = id;
    this.fullName = fullName;
    this.createdAt = createdAt;
    this.updatedAt = null;
  }

  @PreUpdate
  public void beforeUpdate() {
    if (updatedAt == null) {
      this.updatedAt = System.currentTimeMillis();
    }
  }
}
