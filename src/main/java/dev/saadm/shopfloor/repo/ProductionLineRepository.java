package dev.saadm.shopfloor.repo;

import dev.saadm.shopfloor.domain.ProductionLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductionLineRepository extends JpaRepository<ProductionLine, Long> {
    Optional<ProductionLine> findByCode(String code);

    boolean existsByCode(String code);
}
