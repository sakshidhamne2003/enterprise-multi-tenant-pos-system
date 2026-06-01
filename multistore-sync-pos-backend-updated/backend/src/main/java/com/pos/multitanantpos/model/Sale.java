package com.pos.multitanantpos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SaleItem> items = new ArrayList<>();

    public Sale() {}

    public Sale(BigDecimal totalPrice, Tenant tenant, User user) {
        this.totalPrice = totalPrice;
        this.tenant = tenant;
        this.user = user;
    }

    public Long getId() { return id; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public Instant getCreatedAt() { return createdAt; }
    public Tenant getTenant() { return tenant; }
    public User getUser() { return user; }
    public List<SaleItem> getItems() { return items; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setTenant(Tenant tenant) { this.tenant = tenant; }
    public void setUser(User user) { this.user = user; }

    public void addItem(SaleItem item) {
        items.add(item);
        item.setSale(this);
    }
}
