package dev.saadm.shopfloor.service;

import dev.saadm.shopfloor.domain.JobOrder;
import dev.saadm.shopfloor.domain.QcHold;
import dev.saadm.shopfloor.domain.QcHoldStatus;
import dev.saadm.shopfloor.dto.CreateQcHoldRequest;
import dev.saadm.shopfloor.dto.QcHoldResponse;
import dev.saadm.shopfloor.error.BusinessRuleException;
import dev.saadm.shopfloor.error.NotFoundException;
import dev.saadm.shopfloor.repo.JobOrderRepository;
import dev.saadm.shopfloor.repo.QcHoldRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QcService {

    private final QcHoldRepository holds;
    private final JobOrderRepository jobOrders;

    public QcService(QcHoldRepository holds, JobOrderRepository jobOrders) {
        this.holds = holds;
        this.jobOrders = jobOrders;
    }

    @Transactional
    public QcHoldResponse raise(CreateQcHoldRequest req, String raisedBy) {
        JobOrder job = null;
        if (req.jobOrderId() != null) {
            job = jobOrders.findById(req.jobOrderId())
                    .orElseThrow(() -> new NotFoundException("Job order " + req.jobOrderId() + " not found"));
        }
        QcHold saved = holds.save(new QcHold(job, req.reason(), req.severity(), raisedBy));
        return QcHoldResponse.from(saved);
    }

    @Transactional
    public QcHoldResponse release(Long id) {
        QcHold hold = holds.findById(id)
                .orElseThrow(() -> new NotFoundException("QC hold " + id + " not found"));
        if (hold.getStatus() == QcHoldStatus.RELEASED) {
            throw new BusinessRuleException("QC hold " + id + " is already released");
        }
        hold.release();
        return QcHoldResponse.from(hold);
    }

    @Transactional(readOnly = true)
    public List<QcHoldResponse> list(QcHoldStatus status) {
        List<QcHold> result = (status == null)
                ? holds.findAllByOrderByRaisedAtDesc()
                : holds.findByStatusOrderByRaisedAtDesc(status);
        return result.stream().map(QcHoldResponse::from).toList();
    }
}
