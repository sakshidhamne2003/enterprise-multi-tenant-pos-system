package com.pos.multitanantpos.service;

import com.pos.multitanantpos.dto.BillRequest;
import com.pos.multitanantpos.model.*;
import com.pos.multitanantpos.repository.InventoryRepository;
import com.pos.multitanantpos.repository.ProductRepository;
import com.pos.multitanantpos.repository.SaleRepository;
import com.pos.multitanantpos.util.TenantContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BillingService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final SaleRepository saleRepository;
    private final TenantService tenantService;
    private final UserService userService;

    public BillingService(ProductRepository productRepository,
                          InventoryRepository inventoryRepository,
                          SaleRepository saleRepository,
                          TenantService tenantService,
                          UserService userService) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.saleRepository = saleRepository;
        this.tenantService = tenantService;
        this.userService = userService;
    }

    @Transactional
    public Sale createSale(BillRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Bill must have at least one item.");
        }

        Long tenantId = assertTenantId();
        Tenant tenant = tenantService.findById(tenantId);
        User user = findCurrentUser();

        Sale sale = new Sale(BigDecimal.ZERO, tenant, user);
        BigDecimal total = BigDecimal.ZERO;

        for (BillRequest.BillItem item : request.getItems()) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0.");
            }

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product not found with id: " + item.getProductId()));

            // Ensure product belongs to this tenant
            if (!product.getTenant().getId().equals(tenantId)) {
                throw new IllegalStateException("Access denied. Product does not belong to your store.");
            }

            Inventory inventory = inventoryRepository
                    .findByTenantIdAndProductId(tenantId, product.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No inventory found for product: " + product.getName()));

            if (inventory.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for '" + product.getName() +
                        "'. Available: " + inventory.getQuantity() +
                        ", Requested: " + item.getQuantity());
            }

            // Deduct inventory
            inventory.setQuantity(inventory.getQuantity() - item.getQuantity());
            inventoryRepository.save(inventory);

            // Add sale item
            SaleItem saleItem = new SaleItem(product, item.getQuantity(), product.getPrice());
            sale.addItem(saleItem);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        sale.setTotalPrice(total);
        return saleRepository.save(sale);
    }

    private Long assertTenantId() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant context not set. Please login again.");
        }
        return tenantId;
    }

    private User findCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new IllegalStateException("User not authenticated.");
        }
        return userService.findByUsername(auth.getName());
    }
}
