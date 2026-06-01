package dev.saadm.shopfloor.service;

import dev.saadm.shopfloor.domain.JobOrder;
import dev.saadm.shopfloor.domain.JobStatus;
import dev.saadm.shopfloor.domain.ProductionLine;
import dev.saadm.shopfloor.dto.CloseJobOrderRequest;
import dev.saadm.shopfloor.dto.CreateJobOrderRequest;
import dev.saadm.shopfloor.dto.JobOrderResponse;
import dev.saadm.shopfloor.error.BusinessRuleException;
import dev.saadm.shopfloor.error.NotFoundException;
import dev.saadm.shopfloor.repo.DowntimeEventRepository;
import dev.saadm.shopfloor.repo.JobOrderRepository;
import dev.saadm.shopfloor.repo.ProductionLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobOrderService {

    private final JobOrderRepository jobOrders;
    private final ProductionLineRepository lines;
    private final DowntimeEventRepository downtimeEvents;
    private final OeeCalculator oeeCalculator;

    public JobOrderService(JobOrderRepository jobOrders,
                           ProductionLineRepository lines,
                           DowntimeEventRepository downtimeEvents,
                           OeeCalculator oeeCalculator) {
        this.jobOrders = jobOrders;
        this.lines = lines;
        this.downtimeEvents = downtimeEvents;
        this.oeeCalculator = oeeCalculator;
    }

    @Transactional
    public JobOrderResponse create(CreateJobOrderRequest req) {
        if (jobOrders.existsByOrderNo(req.orderNo())) {
            throw new BusinessRuleException("Job order '" + req.orderNo() + "' already exists");
        }
        ProductionLine line = lines.findById(req.lineId())
                .orElseThrow(() -> new NotFoundException("Line " + req.lineId() + " not found"));
        JobOrder saved = jobOrders.save(new JobOrder(
                req.orderNo(), line, req.product(), req.plannedQty(), req.plannedRuntimeMinutes()));
        return JobOrderResponse.from(saved);
    }

    @Transactional
    public JobOrderResponse start(Long id) {
        JobOrder job = find(id);
        if (job.getStatus() != JobStatus.PLANNED) {
            throw new BusinessRuleException("Job order is " + job.getStatus() + "; only PLANNED orders can be started");
        }
        job.setStatus(JobStatus.RUNNING);
        job.setStartedAt(LocalDateTime.now());
        return JobOrderResponse.from(job);
    }

    /** Records the run results, totals logged downtime, and computes + stores OEE. */
    @Transactional
    public JobOrderResponse close(Long id, CloseJobOrderRequest req) {
        JobOrder job = find(id);
        if (job.getStatus() == JobStatus.CLOSED) {
            throw new BusinessRuleException("Job order " + id + " is already closed");
        }
        int downtime = downtimeEvents.totalMinutesForJobOrder(id);
        OeeResult oee = oeeCalculator.compute(
                job.getPlannedRuntimeMinutes(),
                downtime,
                job.getLine().getRatedUnitsPerHour(),
                req.goodUnits(),
                req.rejectUnits());

        job.setGoodUnits(req.goodUnits());
        job.setRejectUnits(req.rejectUnits());
        job.setDowntimeMinutes(downtime);
        job.setAvailability(oee.availability());
        job.setPerformance(oee.performance());
        job.setQuality(oee.quality());
        job.setOee(oee.oee());
        job.setStatus(JobStatus.CLOSED);
        job.setClosedAt(LocalDateTime.now());
        return JobOrderResponse.from(job);
    }

    @Transactional(readOnly = true)
    public JobOrderResponse get(Long id) {
        return JobOrderResponse.from(find(id));
    }

    @Transactional(readOnly = true)
    public List<JobOrderResponse> list() {
        return jobOrders.findAll().stream()
                .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                .map(JobOrderResponse::from)
                .toList();
    }

    private JobOrder find(Long id) {
        return jobOrders.findById(id)
                .orElseThrow(() -> new NotFoundException("Job order " + id + " not found"));
    }
}
