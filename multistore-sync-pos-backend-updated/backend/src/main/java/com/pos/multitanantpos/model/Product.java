package com.pos.multitanantpos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    public Product() {}

    public Product(String name, String description, BigDecimal price, Tenant tenant) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.tenant = tenant;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Instant getCreatedAt() { return createdAt; }
    public Tenant getTenant() { return tenant; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setTenant(Tenant tenant) { this.tenant = tenant; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
