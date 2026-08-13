package dev.saadm.shopfloor.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Pure, dependency-free OEE math — the heart of the service.
 *
 * <pre>
 *   OEE = Availability × Performance × Quality
 *
 *   Availability = run time / planned production time
 *                  (run time = planned − downtime)
 *   Performance  = ideal time to make the units produced / run time
 *                  (ideal time uses the line's rated units/hour)
 *   Quality      = good units / total units
 * </pre>
 *
 * Each factor is clamped to [0, 1] and rounded to 4 decimals.
 */
@Component
public class OeeCalculator {

    private static final int SCALE = 4;
    private static final int WORKING_SCALE = 8;

    public OeeResult compute(int plannedRuntimeMinutes,
                             int downtimeMinutes,
                             int ratedUnitsPerHour,
                             int goodUnits,
                             int rejectUnits) {

        int totalUnits = goodUnits + rejectUnits;
        int runTimeMinutes = Math.max(0, plannedRuntimeMinutes - downtimeMinutes);

        BigDecimal availability = ratio(
                BigDecimal.valueOf(runTimeMinutes),
                BigDecimal.valueOf(plannedRuntimeMinutes));

        BigDecimal idealMinutes = ratedUnitsPerHour <= 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf((long) totalUnits * 60)
                        .divide(BigDecimal.valueOf(ratedUnitsPerHour), WORKING_SCALE, RoundingMode.HALF_UP);
        BigDecimal performance = ratio(idealMinutes, BigDecimal.valueOf(runTimeMinutes));

        BigDecimal quality = ratio(
                BigDecimal.valueOf(goodUnits),
                BigDecimal.valueOf(totalUnits));

        BigDecimal oee = availability.multiply(performance).multiply(quality)
                .setScale(SCALE, RoundingMode.HALF_UP);

        return new OeeResult(availability, performance, quality, oee);
    }

    /** numerator / denominator, clamped to [0, 1], scale 4. Guards divide-by-zero. */
    private BigDecimal ratio(BigDecimal numerator, BigDecimal denominator) {
        if (denominator.signum() <= 0) {
            return BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
        }
        BigDecimal r = numerator.divide(denominator, WORKING_SCALE, RoundingMode.HALF_UP);
        if (r.compareTo(BigDecimal.ONE) > 0) {
            r = BigDecimal.ONE;
        } else if (r.signum() < 0) {
            r = BigDecimal.ZERO;
        }
        return r.setScale(SCALE, RoundingMode.HALF_UP);
    }
}
