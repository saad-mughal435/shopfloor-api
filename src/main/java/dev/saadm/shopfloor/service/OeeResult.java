package dev.saadm.shopfloor.service;

import java.math.BigDecimal;

/** The three OEE factors and their product, each a 0..1 fraction (scale 4). */
public record OeeResult(
        BigDecimal availability,
        BigDecimal performance,
        BigDecimal quality,
        BigDecimal oee) {
}
