package dev.saadm.shopfloor.web;

import dev.saadm.shopfloor.dto.CloseJobOrderRequest;
import dev.saadm.shopfloor.dto.CreateJobOrderRequest;
import dev.saadm.shopfloor.dto.DowntimeRequest;
import dev.saadm.shopfloor.dto.DowntimeResponse;
import dev.saadm.shopfloor.dto.JobOrderResponse;
import dev.saadm.shopfloor.service.DowntimeService;
import dev.saadm.shopfloor.service.JobOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-orders")
@Tag(name = "Job Orders", description = "Plan, run, log downtime, and close production runs (computes OEE)")
public class JobOrderController {

    private final JobOrderService jobOrderService;
    private final DowntimeService downtimeService;

    public JobOrderController(JobOrderService jobOrderService, DowntimeService downtimeService) {
        this.jobOrderService = jobOrderService;
        this.downtimeService = downtimeService;
    }

    @GetMapping
    public List<JobOrderResponse> list() {
        return jobOrderService.list();
    }

    @GetMapping("/{id}")
    public JobOrderResponse get(@PathVariable Long id) {
        return jobOrderService.get(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public JobOrderResponse create(@Valid @RequestBody CreateJobOrderRequest request) {
        return jobOrderService.create(request);
    }

    @PostMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public JobOrderResponse start(@PathVariable Long id) {
        return jobOrderService.start(id);
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public JobOrderResponse close(@PathVariable Long id, @Valid @RequestBody CloseJobOrderRequest request) {
        return jobOrderService.close(id, request);
    }

    @PostMapping("/{id}/downtime")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public DowntimeResponse logDowntime(@PathVariable Long id, @Valid @RequestBody DowntimeRequest request) {
        return downtimeService.log(id, request);
    }

    @GetMapping("/{id}/downtime")
    public List<DowntimeResponse> listDowntime(@PathVariable Long id) {
        return downtimeService.list(id);
    }
}
