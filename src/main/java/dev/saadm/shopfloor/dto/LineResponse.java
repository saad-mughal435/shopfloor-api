package dev.saadm.shopfloor.dto;

import dev.saadm.shopfloor.domain.ProductionLine;

public record LineResponse(Long id, String code, String name, int ratedUnitsPerHour) {
    public static LineResponse from(ProductionLine l) {
        return new LineResponse(l.getId(), l.getCode(), l.getName(), l.getRatedUnitsPerHour());
    }
}
