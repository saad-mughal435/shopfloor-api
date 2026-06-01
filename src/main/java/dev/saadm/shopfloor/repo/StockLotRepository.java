package dev.saadm.shopfloor.repo;

import dev.saadm.shopfloor.domain.StockLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface StockLotRepository extends JpaRepository<StockLot, Long> {

    /** Open lots for an item, oldest first — the consumption order for FIFO issues. */
    List<StockLot> findByItemIdAndQuantityRemainingGreaterThanOrderByReceivedAtAsc(Long itemId, BigDecimal threshold);

    @Query("select coalesce(sum(l.quantityRemaining), 0) from StockLot l where l.item.id = :itemId")
    BigDecimal onHandForItem(@Param("itemId") Long itemId);
}
