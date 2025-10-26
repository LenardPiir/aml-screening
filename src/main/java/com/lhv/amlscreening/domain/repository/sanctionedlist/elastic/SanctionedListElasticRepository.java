package com.lhv.amlscreening.domain.repository.sanctionedlist.elastic;

import com.lhv.amlscreening.domain.entity.SanctionedListEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanctionedListElasticRepository
    extends ElasticsearchRepository<SanctionedListEntity, UUID> {

  @Query(
      """
    {
        "match": {
            "fullName": {
                "query": "?0",
                "fuzziness": "AUTO",
                "minimum_should_match": "80%"
            }
        }
    }
    """)
  List<SanctionedListEntity> findByFullNameFuzzy(String fullName);
}
