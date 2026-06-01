package dev.saadm.shopfloor.web;

import dev.saadm.shopfloor.domain.QcHoldStatus;
import dev.saadm.shopfloor.dto.CreateQcHoldRequest;
import dev.saadm.shopfloor.dto.QcHoldResponse;
import dev.saadm.shopfloor.service.QcService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/qc")
@Tag(name = "Quality", description = "Raise and release QC holds")
public class QcController {

    private final QcService qcService;

    public QcController(QcService qcService) {
        this.qcService = qcService;
    }

    @GetMapping("/holds")
    public List<QcHoldResponse> list(@RequestParam(required = false) QcHoldStatus status) {
        return qcService.list(status);
    }

    @PostMapping("/holds")
    @PreAuthorize("hasAnyRole('QC', 'MANAGER')")
    public QcHoldResponse raise(@Valid @RequestBody CreateQcHoldRequest request, Authentication authentication) {
        return qcService.raise(request, authentication.getName());
    }

    @PostMapping("/holds/{id}/release")
    @PreAuthorize("hasAnyRole('QC', 'MANAGER')")
    public QcHoldResponse release(@PathVariable Long id) {
        return qcService.release(id);
    }
}
