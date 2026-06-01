package com.pos.multitanantpos.service;

import com.pos.multitanantpos.dto.SaleResponse;
import com.pos.multitanantpos.model.Sale;
import com.pos.multitanantpos.model.SaleItem;
import com.pos.multitanantpos.repository.SaleRepository;
import com.pos.multitanantpos.util.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final SaleRepository saleRepository;

    public ReportService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Transactional(readOnly = true)
    public List<SaleResponse> listRecentSales() {
        Long tenantId = assertTenantId();
        return saleRepository.findByTenantIdOrderByCreatedAtDesc(tenantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SaleResponse findById(Long saleId) {
        Long tenantId = assertTenantId();
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + saleId));

        if (!tenantId.equals(sale.getTenant().getId())) {
            throw new IllegalStateException("Access denied. This order does not belong to your store.");
        }
        return toResponse(sale);
    }

    // Public method used by BillingController after creating a sale
    public SaleResponse toResponse(Sale sale) {
        SaleResponse response = new SaleResponse();
        response.setId(sale.getId());
        response.setTotalPrice(sale.getTotalPrice());
        response.setCreatedAt(sale.getCreatedAt());

        List<SaleResponse.SaleItemResponse> items = sale.getItems()
                .stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());
        response.setItems(items);
        return response;
    }

    private SaleResponse.SaleItemResponse toItemResponse(SaleItem item) {
        SaleResponse.SaleItemResponse r = new SaleResponse.SaleItemResponse();
        r.setProductId(item.getProduct().getId());
        r.setProductName(item.getProduct().getName());
        r.setQuantity(item.getQuantity());
        r.setPrice(item.getPrice());
        return r;
    }

    private Long assertTenantId() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant context not set. Please login again.");
        }
        return tenantId;
    }
}
