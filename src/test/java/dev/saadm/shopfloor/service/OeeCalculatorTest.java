package dev.saadm.shopfloor.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class OeeCalculatorTest {

    private final OeeCalculator calc = new OeeCalculator();

    @Test
    void computesEachFactorAndTheirProduct() {
        // planned 480 min, downtime 60 -> run 420; rated 600/hr; 4000 total (3800 good, 200 reject)
        OeeResult r = calc.compute(480, 60, 600, 3800, 200);

        assertThat(r.availability()).isEqualByComparingTo("0.8750"); // 420 / 480
        assertThat(r.performance()).isEqualByComparingTo("0.9524");  // ideal 400 min / 420 run
        assertThat(r.quality()).isEqualByComparingTo("0.9500");      // 3800 / 4000
        assertThat(r.oee()).isEqualByComparingTo("0.7917");          // 0.8750 * 0.9524 * 0.9500
    }

    @Test
    void clampsFactorsToOneHundredPercent() {
        // ran the full planned time, produced above rated, zero rejects -> every factor caps at 1.0
        OeeResult r = calc.compute(100, 0, 60, 1000, 0);

        assertThat(r.availability()).isEqualByComparingTo("1.0000");
        assertThat(r.performance()).isEqualByComparingTo("1.0000");
        assertThat(r.quality()).isEqualByComparingTo("1.0000");
        assertThat(r.oee()).isEqualByComparingTo("1.0000");
    }

    @Test
    void guardsAgainstZeroDenominators() {
        OeeResult r = calc.compute(0, 0, 0, 0, 0);
        assertThat(r.oee()).isEqualByComparingTo("0.0000");
    }

    @Test
    void floorsNegativeFactorsAtZero() {
        // A negative good count cannot arrive through the API (CloseJobOrderRequest is
        // @PositiveOrZero), but the calculator is a public pure function and its contract
        // says [0, 1] — so it has to hold on its own, not because a caller is careful.
        // -5 good of 5 total would give quality -1.0000 without the lower clamp.
        OeeResult r = calc.compute(480, 30, 100, -5, 10);

        assertThat(r.quality()).isEqualByComparingTo("0.0000");
        assertThat(r.oee()).isEqualByComparingTo("0.0000");
    }

    @Test
    void everyFactorStaysWithinTheDocumentedRange() {
        // Sweep the sign combinations the record's own Javadoc promises to survive.
        int[][] cases = {
                {480, 30, 100, -5, 10},     // negative good units
                {480, 30, 100, 10, -5},     // negative rejects -> quality would exceed 1
                {480, 30, 100, -20, 10},    // negative total units
                {480, 600, 100, 100, 0},    // downtime beyond planned
                {-480, 30, 100, 100, 0},    // negative planned runtime
        };

        for (int[] c : cases) {
            OeeResult r = calc.compute(c[0], c[1], c[2], c[3], c[4]);

            assertThat(r.availability()).isBetween(BigDecimal.ZERO, BigDecimal.ONE);
            assertThat(r.performance()).isBetween(BigDecimal.ZERO, BigDecimal.ONE);
            assertThat(r.quality()).isBetween(BigDecimal.ZERO, BigDecimal.ONE);
            assertThat(r.oee()).isBetween(BigDecimal.ZERO, BigDecimal.ONE);
        }
    }
}
