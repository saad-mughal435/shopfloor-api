package dev.saadm.shopfloor.service;

import dev.saadm.shopfloor.domain.DowntimeEvent;
import dev.saadm.shopfloor.domain.JobOrder;
import dev.saadm.shopfloor.domain.JobStatus;
import dev.saadm.shopfloor.dto.DowntimeRequest;
import dev.saadm.shopfloor.dto.DowntimeResponse;
import dev.saadm.shopfloor.error.BusinessRuleException;
import dev.saadm.shopfloor.error.NotFoundException;
import dev.saadm.shopfloor.repo.DowntimeEventRepository;
import dev.saadm.shopfloor.repo.JobOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DowntimeService {

    private final DowntimeEventRepository events;
    private final JobOrderRepository jobOrders;

    public DowntimeService(DowntimeEventRepository events, JobOrderRepository jobOrders) {
        this.events = events;
        this.jobOrders = jobOrders;
    }

    @Transactional
    public DowntimeResponse log(Long jobOrderId, DowntimeRequest req) {
        JobOrder job = jobOrders.findById(jobOrderId)
                .orElseThrow(() -> new NotFoundException("Job order " + jobOrderId + " not found"));
        if (job.getStatus() == JobStatus.CLOSED) {
            throw new BusinessRuleException("Cannot log downtime against a closed job order");
        }
        DowntimeEvent saved = events.save(new DowntimeEvent(job, req.minutes(), req.reason(), req.rootCause()));
        return DowntimeResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<DowntimeResponse> list(Long jobOrderId) {
        return events.findByJobOrderId(jobOrderId).stream().map(DowntimeResponse::from).toList();
    }
}
