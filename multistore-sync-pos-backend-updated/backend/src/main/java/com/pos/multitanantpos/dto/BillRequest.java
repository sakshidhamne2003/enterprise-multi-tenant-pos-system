package com.pos.multitanantpos.dto;

import java.util.ArrayList;
import java.util.List;

public class BillRequest {
    private List<BillItem> items = new ArrayList<>();

    public List<BillItem> getItems() { return items; }
    public void setItems(List<BillItem> items) { this.items = items; }

    public static class BillItem {
        private Long productId;
        private Integer quantity;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}
