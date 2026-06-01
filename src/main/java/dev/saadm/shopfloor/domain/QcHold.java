package dev.saadm.shopfloor.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "qc_hold")
public class QcHold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_order_id")
    private JobOrder jobOrder;

    @Column(nullable = false, length = 255)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QcSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QcHoldStatus status = QcHoldStatus.OPEN;

    @Column(name = "raised_by", nullable = false, length = 64)
    private String raisedBy;

    @Column(name = "raised_at", nullable = false)
    private LocalDateTime raisedAt = LocalDateTime.now();

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    protected QcHold() {
    }

    public QcHold(JobOrder jobOrder, String reason, QcSeverity severity, String raisedBy) {
        this.jobOrder = jobOrder;
        this.reason = reason;
        this.severity = severity;
        this.raisedBy = raisedBy;
    }

    public void release() {
        this.status = QcHoldStatus.RELEASED;
        this.releasedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public JobOrder getJobOrder() {
        return jobOrder;
    }

    public String getReason() {
        return reason;
    }

    public QcSeverity getSeverity() {
        return severity;
    }

    public QcHoldStatus getStatus() {
        return status;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public LocalDateTime getRaisedAt() {
        return raisedAt;
    }

    public LocalDateTime getReleasedAt() {
        return releasedAt;
    }
}
