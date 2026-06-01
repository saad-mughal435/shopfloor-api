package dev.saadm.shopfloor;

import dev.saadm.shopfloor.domain.InventoryItem;
import dev.saadm.shopfloor.domain.StockLot;
import dev.saadm.shopfloor.dto.IssueRequest;
import dev.saadm.shopfloor.dto.ItemResponse;
import dev.saadm.shopfloor.error.BusinessRuleException;
import dev.saadm.shopfloor.repo.InventoryItemRepository;
import dev.saadm.shopfloor.repo.StockLotRepository;
import dev.saadm.shopfloor.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class InventoryFifoTest {

    @Autowired
    private InventoryService inventory;

    @Autowired
    private InventoryItemRepository items;

    @Autowired
    private StockLotRepository lots;

    @Test
    void issuesConsumeTheOldestLotsFirst() {
        InventoryItem item = items.save(new InventoryItem("FIFO-TEST", "FIFO test item", "EA"));
        StockLot older = lots.save(new StockLot(item, new BigDecimal("100.000"), new BigDecimal("1.0000"),
                LocalDateTime.now().minusDays(2)));
        StockLot newer = lots.save(new StockLot(item, new BigDecimal("50.000"), new BigDecimal("1.2000"),
                LocalDateTime.now().minusDays(1)));

        ItemResponse afterIssue = inventory.issue(new IssueRequest("FIFO-TEST", new BigDecimal("120"), "test consume"));

        assertThat(afterIssue.onHand()).isEqualByComparingTo("30");
        assertThat(lots.findById(older.getId()).orElseThrow().getQuantityRemaining()).isEqualByComparingTo("0");
        assertThat(lots.findById(newer.getId()).orElseThrow().getQuantityRemaining()).isEqualByComparingTo("30");
    }

    @Test
    void issuingMoreThanOnHandIsRejected() {
        InventoryItem item = items.save(new InventoryItem("FIFO-SHORT", "short item", "EA"));
        lots.save(new StockLot(item, new BigDecimal("10.000"), new BigDecimal("1.0000"), LocalDateTime.now()));

        assertThatThrownBy(() -> inventory.issue(new IssueRequest("FIFO-SHORT", new BigDecimal("11"), "too much")))
                .isInstanceOf(BusinessRuleException.class);
    }
}
