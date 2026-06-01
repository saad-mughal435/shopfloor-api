package dev.saadm.shopfloor.service;

import dev.saadm.shopfloor.domain.JobOrder;
import dev.saadm.shopfloor.domain.JobStatus;
import dev.saadm.shopfloor.domain.ProductionLine;
import dev.saadm.shopfloor.dto.CreateLineRequest;
import dev.saadm.shopfloor.dto.LineOeeResponse;
import dev.saadm.shopfloor.dto.LineResponse;
import dev.saadm.shopfloor.error.BusinessRuleException;
import dev.saadm.shopfloor.error.NotFoundException;
import dev.saadm.shopfloor.repo.JobOrderRepository;
import dev.saadm.shopfloor.repo.ProductionLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class LineService {

    private final ProductionLineRepository lines;
    private final JobOrderRepository jobOrders;

    public LineService(ProductionLineRepository lines, JobOrderRepository jobOrders) {
        this.lines = lines;
        this.jobOrders = jobOrders;
    }

    @Transactional
    public LineResponse create(CreateLineRequest req) {
        if (lines.existsByCode(req.code())) {
            throw new BusinessRuleException("A line with code '" + req.code() + "' already exists");
        }
        ProductionLine saved = lines.save(new ProductionLine(req.code(), req.name(), req.ratedUnitsPerHour()));
        return LineResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> list() {
        return lines.findAll().stream().map(LineResponse::from).toList();
    }

    /** Rolling OEE for a line = the mean of each factor across its closed job orders. */
    @Transactional(readOnly = true)
    public LineOeeResponse oee(Long lineId) {
        ProductionLine line = lines.findById(lineId)
                .orElseThrow(() -> new NotFoundException("Line " + lineId + " not found"));
        List<JobOrder> closed = jobOrders.findByLineIdAndStatus(lineId, JobStatus.CLOSED);
        if (closed.isEmpty()) {
            BigDecimal z = BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
            return new LineOeeResponse(line.getId(), line.getCode(), 0, z, z, z, z);
        }
        BigDecimal a = mean(closed, JobOrder::getAvailability);
        BigDecimal p = mean(closed, JobOrder::getPerformance);
        BigDecimal q = mean(closed, JobOrder::getQuality);
        BigDecimal o = mean(closed, JobOrder::getOee);
        return new LineOeeResponse(line.getId(), line.getCode(), closed.size(), a, p, q, o);
    }

    private BigDecimal mean(List<JobOrder> jobs, java.util.function.Function<JobOrder, BigDecimal> field) {
        BigDecimal sum = BigDecimal.ZERO;
        for (JobOrder j : jobs) {
            BigDecimal v = field.apply(j);
            if (v != null) {
                sum = sum.add(v);
            }
        }
        return sum.divide(BigDecimal.valueOf(jobs.size()), 4, RoundingMode.HALF_UP);
    }
}
