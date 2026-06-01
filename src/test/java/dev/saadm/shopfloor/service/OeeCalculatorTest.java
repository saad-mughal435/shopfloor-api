package dev.saadm.shopfloor.service;

import org.junit.jupiter.api.Test;

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
}
