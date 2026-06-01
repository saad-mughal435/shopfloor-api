package dev.saadm.shopfloor.repo;

import dev.saadm.shopfloor.domain.JobOrder;
import dev.saadm.shopfloor.domain.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobOrderRepository extends JpaRepository<JobOrder, Long> {
    boolean existsByOrderNo(String orderNo);

    List<JobOrder> findByLineId(Long lineId);

    List<JobOrder> findByLineIdAndStatus(Long lineId, JobStatus status);

    List<JobOrder> findByStatusOrderByClosedAtDesc(JobStatus status);
}
