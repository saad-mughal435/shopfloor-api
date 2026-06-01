package dev.saadm.shopfloor.web;

import dev.saadm.shopfloor.dto.CreateItemRequest;
import dev.saadm.shopfloor.dto.IssueRequest;
import dev.saadm.shopfloor.dto.ItemResponse;
import dev.saadm.shopfloor.dto.MovementResponse;
import dev.saadm.shopfloor.dto.ReceiptRequest;
import dev.saadm.shopfloor.service.InventoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory", description = "Items, FIFO receipts/issues, and the movement ledger")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<ItemResponse> list() {
        return inventoryService.list();
    }

    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ItemResponse createItem(@Valid @RequestBody CreateItemRequest request) {
        return inventoryService.createItem(request);
    }

    @PostMapping("/receipts")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ItemResponse receive(@Valid @RequestBody ReceiptRequest request) {
        return inventoryService.receive(request);
    }

    @PostMapping("/issues")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ItemResponse issue(@Valid @RequestBody IssueRequest request) {
        return inventoryService.issue(request);
    }

    @GetMapping("/{sku}/movements")
    public List<MovementResponse> movements(@PathVariable String sku) {
        return inventoryService.movements(sku);
    }
}
