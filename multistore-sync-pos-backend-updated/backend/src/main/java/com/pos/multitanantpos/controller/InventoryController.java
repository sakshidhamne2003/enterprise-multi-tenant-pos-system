package com.pos.multitanantpos.controller;

import com.pos.multitanantpos.dto.InventoryDTO;
import com.pos.multitanantpos.model.Inventory;
import com.pos.multitanantpos.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:5173")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Inventory> createOrUpdate(@RequestBody InventoryDTO dto) {
        return ResponseEntity.ok(inventoryService.createOrUpdate(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','CASHIER')")
    public ResponseEntity<List<Inventory>> list() {
        return ResponseEntity.ok(inventoryService.list());
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Inventory> update(@PathVariable Long productId,
                                            @RequestBody InventoryDTO dto) {
        return ResponseEntity.ok(inventoryService.adjustQuantity(productId, dto.getQuantity()));
    }
}
