package dev.saadm.shopfloor.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "production_line")
public class ProductionLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String code;

    @Column(nullable = false, length = 128)
    private String name;

    /** Rated (ideal) throughput — the denominator for the Performance factor of OEE. */
    @Column(name = "rated_units_per_hour", nullable = false)
    private int ratedUnitsPerHour;

    protected ProductionLine() {
    }

    public ProductionLine(String code, String name, int ratedUnitsPerHour) {
        this.code = code;
        this.name = name;
        this.ratedUnitsPerHour = ratedUnitsPerHour;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getRatedUnitsPerHour() {
        return ratedUnitsPerHour;
    }
}
