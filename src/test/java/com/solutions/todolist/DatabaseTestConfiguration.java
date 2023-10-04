package com.solutions.todolist;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class DatabaseTestConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpassword");

    }
}
