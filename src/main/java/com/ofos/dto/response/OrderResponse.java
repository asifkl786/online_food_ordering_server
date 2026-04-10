package com.ofos.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String status;
    private String paymentStatus;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal deliveryFee;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime estimatedDeliveryTime;
    private String specialInstructions;
    
    // These are the four response DTOs you requested
    private RestaurantInfoResponse restaurant;
    private UserInfoResponse user;
    private DeliveryInfoResponse deliveryInfo;
    private AddressResponse deliveryAddress;
    
    private List<OrderItemResponse> items;
}
