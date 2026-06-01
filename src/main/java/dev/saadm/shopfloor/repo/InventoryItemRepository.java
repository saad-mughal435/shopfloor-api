package dev.saadm.shopfloor.repo;

import dev.saadm.shopfloor.domain.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findBySku(String sku);

    boolean existsBySku(String sku);

    List<InventoryItem> findAllByOrderBySkuAsc();
}
