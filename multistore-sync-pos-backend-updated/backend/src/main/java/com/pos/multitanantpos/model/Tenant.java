package com.pos.multitanantpos.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tenants")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String companyEmail;

    private Instant createdAt = Instant.now();

    public Tenant() {}

    public Tenant(String name, String companyEmail) {
        this.name = name;
        this.companyEmail = companyEmail;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCompanyEmail() { return companyEmail; }
    public Instant getCreatedAt() { return createdAt; }
    public void setName(String name) { this.name = name; }
    public void setCompanyEmail(String companyEmail) { this.companyEmail = companyEmail; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
