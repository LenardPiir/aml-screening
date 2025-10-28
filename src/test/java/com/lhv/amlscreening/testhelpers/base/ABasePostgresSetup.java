package com.lhv.amlscreening.testhelpers.base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ABasePostgresSetup {
  private static final String POSTGRES_IMAGE = "postgres:18";

  @Container
  public static PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>(POSTGRES_IMAGE)
          .withDatabaseName("testdb")
          .withUsername("testuser")
          .withPassword("testpassword")
          .withReuse(true)
          .withInitScript("database/create_sanction_list_schema_and_changelog.sql");

  @BeforeAll
  static void setupContainer() {
    postgresContainer.start();
  }

  @AfterAll
  static void tearDownContainer() {
    postgresContainer.stop();
  }

  @DynamicPropertySource
  static void dynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgresContainer::getUsername);
    registry.add("spring.datasource.password", postgresContainer::getPassword);
  }
}
