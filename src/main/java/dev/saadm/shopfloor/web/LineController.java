package dev.saadm.shopfloor.web;

import dev.saadm.shopfloor.dto.CreateLineRequest;
import dev.saadm.shopfloor.dto.LineOeeResponse;
import dev.saadm.shopfloor.dto.LineResponse;
import dev.saadm.shopfloor.service.LineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lines")
@Tag(name = "Lines", description = "Production lines and their rolling OEE")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public List<LineResponse> list() {
        return lineService.list();
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public LineResponse create(@Valid @RequestBody CreateLineRequest request) {
        return lineService.create(request);
    }

    @GetMapping("/{id}/oee")
    public LineOeeResponse oee(@PathVariable Long id) {
        return lineService.oee(id);
    }
}
