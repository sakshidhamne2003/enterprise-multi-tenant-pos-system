package com.pos.multitanantpos.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    private Instant lastUpdated = Instant.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    public Inventory() {}

    public Inventory(Product product, Integer quantity, Tenant tenant) {
        this.product = product;
        this.quantity = quantity;
        this.tenant = tenant;
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public Integer getQuantity() { return quantity; }
    public Instant getLastUpdated() { return lastUpdated; }
    public Tenant getTenant() { return tenant; }
    public void setProduct(Product product) { this.product = product; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }
    public void setTenant(Tenant tenant) { this.tenant = tenant; }
}
