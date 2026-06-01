package dev.saadm.shopfloor.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_item")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String sku;

    @Column(nullable = false, length = 160)
    private String name;

    /** Unit of measure, e.g. EA, KG, L. */
    @Column(nullable = false, length = 16)
    private String uom;

    protected InventoryItem() {
    }

    public InventoryItem(String sku, String name, String uom) {
        this.sku = sku;
        this.name = name;
        this.uom = uom;
    }

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getUom() {
        return uom;
    }
}
