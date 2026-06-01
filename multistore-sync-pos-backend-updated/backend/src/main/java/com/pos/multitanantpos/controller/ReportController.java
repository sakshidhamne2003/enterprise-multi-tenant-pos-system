package com.pos.multitanantpos.controller;

import com.pos.multitanantpos.dto.SaleResponse;
import com.pos.multitanantpos.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:5173")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/sales")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SaleResponse>> sales() {
        return ResponseEntity.ok(reportService.listRecentSales());
    }
}
