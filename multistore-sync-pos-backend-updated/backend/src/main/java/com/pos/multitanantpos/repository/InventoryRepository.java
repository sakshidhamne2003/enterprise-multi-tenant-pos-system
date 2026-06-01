package com.pos.multitanantpos.repository;

import com.pos.multitanantpos.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByTenantId(Long tenantId);
    Optional<Inventory> findByTenantIdAndProductId(Long tenantId, Long productId);
}
