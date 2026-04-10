package com.ofos.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {
    private Long id;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private List<CartItemResponse> items;
}
