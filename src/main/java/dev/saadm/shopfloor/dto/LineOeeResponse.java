package dev.saadm.shopfloor.dto;

import java.math.BigDecimal;

/** Rolling OEE for a line, averaged across its closed job orders. */
public record LineOeeResponse(
        Long lineId,
        String lineCode,
        int closedJobOrders,
        BigDecimal availability,
        BigDecimal performance,
        BigDecimal quality,
        BigDecimal oee) {
}
