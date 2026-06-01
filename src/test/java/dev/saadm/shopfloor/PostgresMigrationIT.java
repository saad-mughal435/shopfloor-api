package dev.saadm.shopfloor;

import dev.saadm.shopfloor.repo.JobOrderRepository;
import dev.saadm.shopfloor.repo.ProductionLineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Boots the whole app against a real PostgreSQL (Testcontainers) under the
 * 'postgres' profile. That means Flyway's V1 migration ran and Hibernate's
 * {@code ddl-auto: validate} passed against it — i.e. the migration and the
 * entity mappings agree. Skipped automatically where Docker isn't available
 * (e.g. a laptop without Docker); runs in CI.
 */
@SpringBootTest
@ActiveProfiles("postgres")
@Testcontainers(disabledWithoutDocker = true)
class PostgresMigrationIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ProductionLineRepository lines;

    @Autowired
    private JobOrderRepository jobOrders;

    @Test
    void migrationMatchesEntitiesAndSeedDataLoads() {
        assertThat(lines.count()).isEqualTo(2);
        assertThat(jobOrders.count()).isGreaterThanOrEqualTo(4);
    }
}
