package dev.saadm.shopfloor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ShopFloor API — a manufacturing operations (MES/OEE) backend.
 *
 * <p>Built by Muhammad Saad to express real beverage-plant operations
 * (production planning, OEE, downtime/root-cause, QC holds, FIFO inventory)
 * in the enterprise Java stack: Spring Boot 3, Spring Data JPA, Spring
 * Security (JWT), Flyway + PostgreSQL, and OpenAPI.
 */
@SpringBootApplication
public class ShopFloorApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopFloorApiApplication.class, args);
    }
}
