package com.ofos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ofos.dto.request.OrderRequest;
import com.ofos.dto.response.OrderResponse;
import com.ofos.entity.OrderStatus;

public interface OrderService {
 
 OrderResponse createOrder(OrderRequest request, String userEmail);
 
 OrderResponse getOrderById(Long orderId);
 
 OrderResponse getOrderByNumber(String orderNumber);
 
 Page<OrderResponse> getUserOrders(String userEmail, Pageable pageable);
 
 OrderResponse updateOrderStatus(Long orderId, OrderStatus status, String userEmail);
 
 OrderResponse assignDeliveryPartner(Long orderId, Long deliveryPartnerId);
 
 OrderResponse cancelOrder(Long orderId, String userEmail);
 
 void processPayment(Long orderId, String paymentDetails);
}
