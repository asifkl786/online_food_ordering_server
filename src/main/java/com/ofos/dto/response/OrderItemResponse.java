package com.ofos.dto.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long menuItemId;
    private String itemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
