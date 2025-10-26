package com.lhv.amlscreening.testhelpers.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ABaseElasticSetup extends ABasePostgresSetup {
  private static final String A_ELASTIC_IMAGE =
      "docker.elastic.co/elasticsearch/elasticsearch:8.18.8";

  @Container
  public static ElasticsearchContainer elasticsearchContainer =
      new ElasticsearchContainer(A_ELASTIC_IMAGE)
          .withExposedPorts(9200)
          .withEnv("xpack.security.enabled", "false") // Makes container use http instead of https
          .withEnv(
              "xpack.security.http.ssl.enabled",
              "false"); // Makes container use http instead of https

  @DynamicPropertySource
  static void dynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress);
  }
}
