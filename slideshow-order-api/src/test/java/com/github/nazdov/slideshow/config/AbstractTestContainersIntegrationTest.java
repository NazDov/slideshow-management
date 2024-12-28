package com.github.nazdov.slideshow.config;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
public abstract class AbstractTestContainersIntegrationTest {

    private static final MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("sm-db")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(3306)
            .withInitScript("init.sql");

    static {
        mysql.start();
        mysql.waitingFor(Wait.forListeningPort());
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", AbstractTestContainersIntegrationTest::r2dbcUrl);
        registry.add("spring.r2dbc.username", mysql::getUsername);
        registry.add("spring.r2dbc.password", mysql::getPassword);
    }

    private static String r2dbcUrl() {
        return String.format("r2dbc:mysql://%s:%d/%s",
                mysql.getHost(),
                mysql.getMappedPort(MySQLContainer.MYSQL_PORT),
                mysql.getDatabaseName());
    }
}
