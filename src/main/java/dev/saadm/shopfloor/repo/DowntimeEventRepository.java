package dev.saadm.shopfloor.repo;

import dev.saadm.shopfloor.domain.DowntimeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DowntimeEventRepository extends JpaRepository<DowntimeEvent, Long> {
    List<DowntimeEvent> findByJobOrderId(Long jobOrderId);

    @Query("select coalesce(sum(d.minutes), 0) from DowntimeEvent d where d.jobOrder.id = :jobOrderId")
    int totalMinutesForJobOrder(@Param("jobOrderId") Long jobOrderId);
}
