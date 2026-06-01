package dev.saadm.shopfloor.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** An immutable audit row for every receipt and issue against an item. */
@Entity
@Table(name = "stock_movement")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private InventoryItem item;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MovementType type;

    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal quantity;

    @Column(length = 120)
    private String reference;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected StockMovement() {
    }

    public StockMovement(InventoryItem item, MovementType type, BigDecimal quantity, String reference) {
        this.item = item;
        this.type = type;
        this.quantity = quantity;
        this.reference = reference;
    }

    public Long getId() {
        return id;
    }

    public InventoryItem getItem() {
        return item;
    }

    public MovementType getType() {
        return type;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getReference() {
        return reference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
