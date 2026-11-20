/*
Creating the PostgreSQL Testcontainer
Removing dependence of manually running PostgreSQL to test integration tests

1. Create a test configuration class: PostgresTestContainerConfig.java
*/

package com.daria.transactionapi;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class PostgresTestContainerConfig {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer("postgres:18.6");
    }
    // PostgreSQLContainer<?> postgresContainer() { return new PostgreSQLContainer<>("postgres:18.6");}
}