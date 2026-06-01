package dev.saadm.shopfloor.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_order")
public class JobOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, unique = true, length = 40)
    private String orderNo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "line_id", nullable = false)
    private ProductionLine line;

    @Column(nullable = false, length = 128)
    private String product;

    @Column(name = "planned_qty", nullable = false)
    private int plannedQty;

    @Column(name = "planned_runtime_minutes", nullable = false)
    private int plannedRuntimeMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JobStatus status = JobStatus.PLANNED;

    // --- populated on close ---
    @Column(name = "good_units")
    private Integer goodUnits;

    @Column(name = "reject_units")
    private Integer rejectUnits;

    @Column(name = "downtime_minutes")
    private Integer downtimeMinutes;

    @Column(precision = 5, scale = 4)
    private BigDecimal availability;

    @Column(precision = 5, scale = 4)
    private BigDecimal performance;

    @Column(precision = 5, scale = 4)
    private BigDecimal quality;

    @Column(precision = 5, scale = 4)
    private BigDecimal oee;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    protected JobOrder() {
    }

    public JobOrder(String orderNo, ProductionLine line, String product, int plannedQty, int plannedRuntimeMinutes) {
        this.orderNo = orderNo;
        this.line = line;
        this.product = product;
        this.plannedQty = plannedQty;
        this.plannedRuntimeMinutes = plannedRuntimeMinutes;
    }

    public Long getId() {
        return id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public ProductionLine getLine() {
        return line;
    }

    public String getProduct() {
        return product;
    }

    public int getPlannedQty() {
        return plannedQty;
    }

    public int getPlannedRuntimeMinutes() {
        return plannedRuntimeMinutes;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public Integer getGoodUnits() {
        return goodUnits;
    }

    public void setGoodUnits(Integer goodUnits) {
        this.goodUnits = goodUnits;
    }

    public Integer getRejectUnits() {
        return rejectUnits;
    }

    public void setRejectUnits(Integer rejectUnits) {
        this.rejectUnits = rejectUnits;
    }

    public Integer getDowntimeMinutes() {
        return downtimeMinutes;
    }

    public void setDowntimeMinutes(Integer downtimeMinutes) {
        this.downtimeMinutes = downtimeMinutes;
    }

    public BigDecimal getAvailability() {
        return availability;
    }

    public void setAvailability(BigDecimal availability) {
        this.availability = availability;
    }

    public BigDecimal getPerformance() {
        return performance;
    }

    public void setPerformance(BigDecimal performance) {
        this.performance = performance;
    }

    public BigDecimal getQuality() {
        return quality;
    }

    public void setQuality(BigDecimal quality) {
        this.quality = quality;
    }

    public BigDecimal getOee() {
        return oee;
    }

    public void setOee(BigDecimal oee) {
        this.oee = oee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }
}
