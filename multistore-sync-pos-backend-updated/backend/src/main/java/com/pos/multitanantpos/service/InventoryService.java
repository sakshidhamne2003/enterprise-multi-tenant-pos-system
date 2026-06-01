package com.pos.multitanantpos.service;

import com.pos.multitanantpos.dto.InventoryDTO;
import com.pos.multitanantpos.model.Inventory;
import com.pos.multitanantpos.model.Product;
import com.pos.multitanantpos.model.Tenant;
import com.pos.multitanantpos.repository.InventoryRepository;
import com.pos.multitanantpos.repository.ProductRepository;
import com.pos.multitanantpos.util.TenantContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final TenantService tenantService;

    public InventoryService(InventoryRepository inventoryRepository,
                            ProductRepository productRepository,
                            TenantService tenantService) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.tenantService = tenantService;
    }

    public Inventory createOrUpdate(InventoryDTO dto) {
        Tenant tenant = assertTenant();
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + dto.getProductId()));

        ensureSameTenant(product.getTenant().getId(), tenant.getId());

        Inventory inventory = inventoryRepository
                .findByTenantIdAndProductId(tenant.getId(), product.getId())
                .orElse(new Inventory(product, 0, tenant));

        inventory.setQuantity(dto.getQuantity());
        inventory.setLastUpdated(Instant.now());
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> list() {
        Long tenantId = assertTenantId();
        return inventoryRepository.findByTenantId(tenantId);
    }

    public Inventory adjustQuantity(Long productId, Integer quantity) {
        Tenant tenant = assertTenant();
        Inventory inventory = inventoryRepository
                .findByTenantIdAndProductId(tenant.getId(), productId)
                .orElseThrow(() -> new IllegalArgumentException("No inventory record found for this product."));

        ensureSameTenant(inventory.getTenant().getId(), tenant.getId());
        inventory.setQuantity(quantity);
        inventory.setLastUpdated(Instant.now());
        return inventoryRepository.save(inventory);
    }

    private Tenant assertTenant() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant context not set. Please login again.");
        }
        return tenantService.findById(tenantId);
    }

    private Long assertTenantId() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant context not set. Please login again.");
        }
        return tenantId;
    }

    private void ensureSameTenant(Long target, Long expected) {
        if (target == null || !target.equals(expected)) {
            throw new IllegalStateException("Access denied. This resource does not belong to your store.");
        }
    }
}
