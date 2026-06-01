package com.pos.multitanantpos.controller;

import com.pos.multitanantpos.dto.TenantRequest;
import com.pos.multitanantpos.model.Tenant;
import com.pos.multitanantpos.service.TenantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant")
@CrossOrigin(origins = "http://localhost:5173")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping("/create")
    public ResponseEntity<Tenant> create(@RequestBody TenantRequest request) {
        Tenant tenant = new Tenant(request.getName(), request.getCompanyEmail());
        return ResponseEntity.ok(tenantService.createTenant(tenant));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tenant> get(@PathVariable Long id) {
        return ResponseEntity.ok(tenantService.findById(id));
    }
}
