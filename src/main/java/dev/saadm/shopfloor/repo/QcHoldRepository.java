package dev.saadm.shopfloor.repo;

import dev.saadm.shopfloor.domain.QcHold;
import dev.saadm.shopfloor.domain.QcHoldStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QcHoldRepository extends JpaRepository<QcHold, Long> {
    List<QcHold> findByStatusOrderByRaisedAtDesc(QcHoldStatus status);

    List<QcHold> findAllByOrderByRaisedAtDesc();
}
