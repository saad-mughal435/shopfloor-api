package dev.saadm.shopfloor.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** A received batch of stock. Issues consume the oldest lots first (FIFO). */
@Entity
@Table(name = "stock_lot")
public class StockLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private InventoryItem item;

    @Column(name = "quantity_remaining", nullable = false, precision = 14, scale = 3)
    private BigDecimal quantityRemaining;

    @Column(name = "unit_cost", nullable = false, precision = 14, scale = 4)
    private BigDecimal unitCost;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt = LocalDateTime.now();

    protected StockLot() {
    }

    public StockLot(InventoryItem item, BigDecimal quantityRemaining, BigDecimal unitCost, LocalDateTime receivedAt) {
        this.item = item;
        this.quantityRemaining = quantityRemaining;
        this.unitCost = unitCost;
        this.receivedAt = receivedAt;
    }

    public Long getId() {
        return id;
    }

    public InventoryItem getItem() {
        return item;
    }

    public BigDecimal getQuantityRemaining() {
        return quantityRemaining;
    }

    public void setQuantityRemaining(BigDecimal quantityRemaining) {
        this.quantityRemaining = quantityRemaining;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }
}
