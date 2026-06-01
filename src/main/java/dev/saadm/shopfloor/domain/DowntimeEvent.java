package dev.saadm.shopfloor.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "downtime_event")
public class DowntimeEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_order_id", nullable = false)
    private JobOrder jobOrder;

    @Column(nullable = false)
    private int minutes;

    @Column(nullable = false, length = 160)
    private String reason;

    @Column(name = "root_cause", length = 255)
    private String rootCause;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt = LocalDateTime.now();

    protected DowntimeEvent() {
    }

    public DowntimeEvent(JobOrder jobOrder, int minutes, String reason, String rootCause) {
        this.jobOrder = jobOrder;
        this.minutes = minutes;
        this.reason = reason;
        this.rootCause = rootCause;
    }

    public Long getId() {
        return id;
    }

    public JobOrder getJobOrder() {
        return jobOrder;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getReason() {
        return reason;
    }

    public String getRootCause() {
        return rootCause;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
