package com.pos.multitanantpos.service;

import com.pos.multitanantpos.dto.ProductDTO;
import com.pos.multitanantpos.model.Product;
import com.pos.multitanantpos.model.Tenant;
import com.pos.multitanantpos.repository.ProductRepository;
import com.pos.multitanantpos.util.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final TenantService tenantService;

    public ProductService(ProductRepository productRepository, TenantService tenantService) {
        this.productRepository = productRepository;
        this.tenantService = tenantService;
    }

    public Product create(ProductDTO dto) {
        Tenant tenant = assertTenant();
        Product product = new Product(dto.getName(), dto.getDescription(), dto.getPrice(), tenant);
        return productRepository.save(product);
    }

    public List<Product> list() {
        Long tenantId = assertTenantId();
        return productRepository.findByTenantId(tenantId);
    }

    public Product findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        ensureSameTenant(product.getTenant().getId());
        return product;
    }

    public Product update(Long id, ProductDTO dto) {
        Product product = findById(id);
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        return productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
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

    private void ensureSameTenant(Long productTenantId) {
        Long currentTenantId = TenantContext.getCurrentTenant();
        if (currentTenantId == null || !currentTenantId.equals(productTenantId)) {
            throw new IllegalStateException("Access denied. Product does not belong to your store.");
        }
    }
}
