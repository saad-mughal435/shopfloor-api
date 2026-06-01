package dev.saadm.shopfloor.service;

import dev.saadm.shopfloor.domain.InventoryItem;
import dev.saadm.shopfloor.domain.MovementType;
import dev.saadm.shopfloor.domain.StockLot;
import dev.saadm.shopfloor.domain.StockMovement;
import dev.saadm.shopfloor.dto.CreateItemRequest;
import dev.saadm.shopfloor.dto.IssueRequest;
import dev.saadm.shopfloor.dto.ItemResponse;
import dev.saadm.shopfloor.dto.MovementResponse;
import dev.saadm.shopfloor.dto.ReceiptRequest;
import dev.saadm.shopfloor.error.BusinessRuleException;
import dev.saadm.shopfloor.error.NotFoundException;
import dev.saadm.shopfloor.repo.InventoryItemRepository;
import dev.saadm.shopfloor.repo.StockLotRepository;
import dev.saadm.shopfloor.repo.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryService {

    private final InventoryItemRepository itemRepo;
    private final StockLotRepository lotRepo;
    private final StockMovementRepository movementRepo;

    public InventoryService(InventoryItemRepository itemRepo,
                            StockLotRepository lotRepo,
                            StockMovementRepository movementRepo) {
        this.itemRepo = itemRepo;
        this.lotRepo = lotRepo;
        this.movementRepo = movementRepo;
    }

    @Transactional
    public ItemResponse createItem(CreateItemRequest req) {
        if (itemRepo.existsBySku(req.sku())) {
            throw new BusinessRuleException("Item '" + req.sku() + "' already exists");
        }
        InventoryItem saved = itemRepo.save(new InventoryItem(req.sku(), req.name(), req.uom()));
        return ItemResponse.from(saved, BigDecimal.ZERO.setScale(3));
    }

    /** Goods receipt — opens a new FIFO lot and records the movement. */
    @Transactional
    public ItemResponse receive(ReceiptRequest req) {
        InventoryItem item = requireItem(req.sku());
        lotRepo.save(new StockLot(item, req.quantity(), req.unitCost(), LocalDateTime.now()));
        movementRepo.save(new StockMovement(item, MovementType.RECEIPT, req.quantity(), "Goods receipt"));
        return ItemResponse.from(item, lotRepo.onHandForItem(item.getId()));
    }

    /** Issue stock, consuming the oldest lots first (FIFO). */
    @Transactional
    public ItemResponse issue(IssueRequest req) {
        InventoryItem item = requireItem(req.sku());
        BigDecimal onHand = lotRepo.onHandForItem(item.getId());
        if (req.quantity().compareTo(onHand) > 0) {
            throw new BusinessRuleException(
                    "Insufficient stock for " + req.sku() + ": on hand " + onHand + ", requested " + req.quantity());
        }
        BigDecimal remaining = req.quantity();
        List<StockLot> openLots =
                lotRepo.findByItemIdAndQuantityRemainingGreaterThanOrderByReceivedAtAsc(item.getId(), BigDecimal.ZERO);
        for (StockLot lot : openLots) {
            if (remaining.signum() <= 0) {
                break;
            }
            BigDecimal take = lot.getQuantityRemaining().min(remaining);
            lot.setQuantityRemaining(lot.getQuantityRemaining().subtract(take));
            remaining = remaining.subtract(take);
        }
        movementRepo.save(new StockMovement(item, MovementType.ISSUE, req.quantity(), req.reference()));
        return ItemResponse.from(item, lotRepo.onHandForItem(item.getId()));
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> list() {
        return itemRepo.findAllByOrderBySkuAsc().stream()
                .map(i -> ItemResponse.from(i, lotRepo.onHandForItem(i.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MovementResponse> movements(String sku) {
        InventoryItem item = requireItem(sku);
        return movementRepo.findByItemIdOrderByCreatedAtDesc(item.getId()).stream()
                .map(MovementResponse::from)
                .toList();
    }

    private InventoryItem requireItem(String sku) {
        return itemRepo.findBySku(sku)
                .orElseThrow(() -> new NotFoundException("Item '" + sku + "' not found"));
    }
}
