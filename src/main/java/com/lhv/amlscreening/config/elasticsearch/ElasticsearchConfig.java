package com.lhv.amlscreening.config.elasticsearch;

import com.lhv.amlscreening.common.util.LocalDateTimeToLongConverter;
import com.lhv.amlscreening.common.util.LongToLocalDateTimeConverter;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(
    basePackages = "com.lhv.amlscreening.domain.repository.sanctionedlist.elastic")
public class ElasticsearchConfig {

  @Bean
  public ElasticsearchCustomConversions elasticsearchCustomConversions() {
    return new ElasticsearchCustomConversions(
        Arrays.asList(new LocalDateTimeToLongConverter(), new LongToLocalDateTimeConverter()));
  }
}
