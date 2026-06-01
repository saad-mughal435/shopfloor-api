package dev.saadm.shopfloor.config;

import dev.saadm.shopfloor.domain.AppUser;
import dev.saadm.shopfloor.domain.ProductionLine;
import dev.saadm.shopfloor.domain.QcSeverity;
import dev.saadm.shopfloor.domain.Role;
import dev.saadm.shopfloor.dto.*;
import dev.saadm.shopfloor.repo.AppUserRepository;
import dev.saadm.shopfloor.repo.ProductionLineRepository;
import dev.saadm.shopfloor.service.DowntimeService;
import dev.saadm.shopfloor.service.InventoryService;
import dev.saadm.shopfloor.service.JobOrderService;
import dev.saadm.shopfloor.service.QcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Loads a realistic beverage-plant snapshot on first start (when the user table
 * is empty) so Swagger and the GET endpoints have data immediately. Job orders
 * are driven through the real create→start→downtime→close path, so the seeded
 * OEE figures and FIFO balances are genuinely computed, not hard-coded.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final boolean enabled;
    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final ProductionLineRepository lines;
    private final JobOrderService jobOrders;
    private final DowntimeService downtime;
    private final QcService qc;
    private final InventoryService inventory;

    public DataSeeder(@Value("${app.seed.enabled:true}") boolean enabled,
                      AppUserRepository users,
                      PasswordEncoder passwordEncoder,
                      ProductionLineRepository lines,
                      JobOrderService jobOrders,
                      DowntimeService downtime,
                      QcService qc,
                      InventoryService inventory) {
        this.enabled = enabled;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.lines = lines;
        this.jobOrders = jobOrders;
        this.downtime = downtime;
        this.qc = qc;
        this.inventory = inventory;
    }

    @Override
    public void run(String... args) {
        if (!enabled || users.count() > 0) {
            return;
        }

        // --- users (all share the demo password "password") ---
        users.save(new AppUser("manager", passwordEncoder.encode("password"), Role.MANAGER));
        users.save(new AppUser("qc", passwordEncoder.encode("password"), Role.QC));
        users.save(new AppUser("operator", passwordEncoder.encode("password"), Role.OPERATOR));

        // --- production lines ---
        ProductionLine lineA = lines.save(new ProductionLine("LINE-A", "Krones PET Line A — 0.5L still water", 7200));
        ProductionLine lineB = lines.save(new ProductionLine("LINE-B", "Canning Line B — 330ml", 12000));

        // --- inventory: create item, receive a lot, (one issue to show FIFO + movements) ---
        seedItem("PET-PREFORM-28G", "28g PET preform", "EA", "120000", "0.0180");
        seedItem("CAP-29-25", "29/25 closure cap", "EA", "90000", "0.0090");
        seedItem("LABEL-500ML", "500ml BOPP wrap label", "EA", "75000", "0.0040");
        seedItem("CARTON-12", "12-pack shipping carton", "EA", "6000", "0.1500");
        inventory.issue(new IssueRequest("PET-PREFORM-28G", new BigDecimal("47200"), "JO-2026-001 consumption"));

        // --- job orders driven through the real lifecycle (OEE is computed on close) ---
        JobOrderResponse jo1 = jobOrders.create(new CreateJobOrderRequest(
                "JO-2026-001", lineA.getId(), "0.5L Still Water — 24x0.5L shrink", 50000, 480));
        jobOrders.start(jo1.id());
        downtime.log(jo1.id(), new DowntimeRequest(22, "Filler short stops", "Cap chute jam"));
        downtime.log(jo1.id(), new DowntimeRequest(15, "Label applicator splice", "Reel change"));
        jobOrders.close(jo1.id(), new CloseJobOrderRequest(47200, 900));

        JobOrderResponse jo2 = jobOrders.create(new CreateJobOrderRequest(
                "JO-2026-002", lineB.getId(), "330ml Cola — 24-can tray", 60000, 420));
        jobOrders.start(jo2.id());
        downtime.log(jo2.id(), new DowntimeRequest(35, "Seamer adjustment", "Double-seam out of spec"));
        jobOrders.close(jo2.id(), new CloseJobOrderRequest(57600, 1200));

        JobOrderResponse jo3 = jobOrders.create(new CreateJobOrderRequest(
                "JO-2026-003", lineA.getId(), "0.5L Still Water — 24x0.5L shrink", 40000, 360));
        jobOrders.start(jo3.id());

        jobOrders.create(new CreateJobOrderRequest(
                "JO-2026-004", lineB.getId(), "330ml Orange — 24-can tray", 48000, 360));

        // --- an open QC hold against the canning job ---
        qc.raise(new CreateQcHoldRequest(jo2.id(),
                "Fill level below target on 3 of 20 sampled cans", QcSeverity.MEDIUM), "qc");
    }

    private void seedItem(String sku, String name, String uom, String qty, String unitCost) {
        inventory.createItem(new CreateItemRequest(sku, name, uom));
        inventory.receive(new ReceiptRequest(sku, new BigDecimal(qty), new BigDecimal(unitCost)));
    }
}
