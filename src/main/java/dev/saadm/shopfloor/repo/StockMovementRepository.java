package dev.saadm.shopfloor.repo;

import dev.saadm.shopfloor.domain.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByItemIdOrderByCreatedAtDesc(Long itemId);
}
