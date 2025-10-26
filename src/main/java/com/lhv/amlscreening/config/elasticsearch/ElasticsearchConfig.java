package com.lhv.amlscreening.config.elasticsearch;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(
    basePackages = "com.lhv.amlscreening.domain.repository.sanctionedlist.elastic")
public class ElasticsearchConfig {}
