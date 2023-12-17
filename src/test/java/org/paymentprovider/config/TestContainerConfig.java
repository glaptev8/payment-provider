package org.paymentprovider.config;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class TestContainerConfig {
  private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");

  @BeforeAll
  static void setUp() {
    postgresContainer.start();
    System.setProperty("spring.r2dbc.url", String.format("r2dbc:postgresql://%s:%d/%s", postgresContainer.getHost(), postgresContainer.getFirstMappedPort(), postgresContainer.getDatabaseName()));
    System.setProperty("spring.r2dbc.username", postgresContainer.getUsername());
    System.setProperty("spring.r2dbc.password", postgresContainer.getPassword());
    System.setProperty("spring.flyway.password", postgresContainer.getPassword());
    System.setProperty("spring.flyway.user", postgresContainer.getUsername());
    System.setProperty("spring.flyway.url", String.format("jdbc:postgresql://%s:%d/%s", postgresContainer.getHost(), postgresContainer.getFirstMappedPort(), postgresContainer.getDatabaseName()));
  }
}
