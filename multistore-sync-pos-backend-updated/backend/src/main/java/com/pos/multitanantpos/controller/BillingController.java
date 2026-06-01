package com.pos.multitanantpos.controller;

import com.pos.multitanantpos.dto.BillRequest;
import com.pos.multitanantpos.dto.SaleResponse;
import com.pos.multitanantpos.model.Sale;
import com.pos.multitanantpos.service.BillingService;
import com.pos.multitanantpos.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class BillingController {

    private final BillingService billingService;
    private final ReportService reportService;

    public BillingController(BillingService billingService, ReportService reportService) {
        this.billingService = billingService;
        this.reportService = reportService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','CASHIER')")
    public ResponseEntity<SaleResponse> create(@RequestBody BillRequest request) {
        Sale sale = billingService.createSale(request);
        return ResponseEntity.ok(reportService.toResponse(sale));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<List<SaleResponse>> list() {
        return ResponseEntity.ok(reportService.listRecentSales());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<SaleResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.findById(id));
    }
}
