package dev.saadm.shopfloor.dto;

import dev.saadm.shopfloor.domain.InventoryItem;

import java.math.BigDecimal;

public record ItemResponse(Long id, String sku, String name, String uom, BigDecimal onHand) {
    public static ItemResponse from(InventoryItem i, BigDecimal onHand) {
        return new ItemResponse(i.getId(), i.getSku(), i.getName(), i.getUom(), onHand);
    }
}
