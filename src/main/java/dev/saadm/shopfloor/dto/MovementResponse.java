package dev.saadm.shopfloor.dto;

import dev.saadm.shopfloor.domain.MovementType;
import dev.saadm.shopfloor.domain.StockMovement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementResponse(
        Long id,
        String sku,
        MovementType type,
        BigDecimal quantity,
        String reference,
        LocalDateTime createdAt) {

    public static MovementResponse from(StockMovement m) {
        return new MovementResponse(
                m.getId(), m.getItem().getSku(), m.getType(),
                m.getQuantity(), m.getReference(), m.getCreatedAt());
    }
}
