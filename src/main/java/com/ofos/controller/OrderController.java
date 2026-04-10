package com.ofos.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ofos.dto.request.OrderRequest;
import com.ofos.dto.response.OrderResponse;
import com.ofos.entity.OrderStatus;
import com.ofos.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
 
 private final OrderService orderService;
 
 @PostMapping
 public ResponseEntity<OrderResponse> createOrder(
         @Valid @RequestBody OrderRequest request,
         @AuthenticationPrincipal UserDetails userDetails) {
     log.info("REST request to create order");
     OrderResponse response = orderService.createOrder(request, userDetails.getUsername());
     return ResponseEntity.status(HttpStatus.CREATED).body(response);
 }
 
 @GetMapping("/{orderId}")
 public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
     log.info("REST request to get order by id: {}", orderId);
     OrderResponse response = orderService.getOrderById(orderId);
     return ResponseEntity.ok(response);
 }
 
 @GetMapping("/number/{orderNumber}")
 public ResponseEntity<OrderResponse> getOrderByNumber(@PathVariable String orderNumber) {
     log.info("REST request to get order by number: {}", orderNumber);
     OrderResponse response = orderService.getOrderByNumber(orderNumber);
     return ResponseEntity.ok(response);
 }
 
 @GetMapping("/my-orders")
 public ResponseEntity<Page<OrderResponse>> getMyOrders(
         @AuthenticationPrincipal UserDetails userDetails,
         @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
     log.info("REST request to get my orders");
     Page<OrderResponse> orders = orderService.getUserOrders(userDetails.getUsername(), pageable);
     return ResponseEntity.ok(orders);
 }
 
 @PutMapping("/{orderId}/status")
 public ResponseEntity<OrderResponse> updateOrderStatus(
         @PathVariable Long orderId,
         @RequestParam OrderStatus status,
         @AuthenticationPrincipal UserDetails userDetails) {
     log.info("REST request to update order status: {} for order: {}", status, orderId);
     OrderResponse response = orderService.updateOrderStatus(orderId, status, userDetails.getUsername());
     return ResponseEntity.ok(response);
 }
 
 @PostMapping("/{orderId}/cancel")
 public ResponseEntity<OrderResponse> cancelOrder(
         @PathVariable Long orderId,
         @AuthenticationPrincipal UserDetails userDetails) {
     log.info("REST request to cancel order: {}", orderId);
     OrderResponse response = orderService.cancelOrder(orderId, userDetails.getUsername());
     return ResponseEntity.ok(response);
 }
 
 @PostMapping("/{orderId}/assign-delivery")
 public ResponseEntity<OrderResponse> assignDeliveryPartner(
         @PathVariable Long orderId,
         @RequestParam Long deliveryPartnerId) {
     log.info("REST request to assign delivery partner {} to order {}", deliveryPartnerId, orderId);
     OrderResponse response = orderService.assignDeliveryPartner(orderId, deliveryPartnerId);
     return ResponseEntity.ok(response);
 }
 
 @PostMapping("/{orderId}/payment")
 public ResponseEntity<Void> processPayment(
         @PathVariable Long orderId,
         @RequestBody String paymentDetails) {
     log.info("REST request to process payment for order: {}", orderId);
     orderService.processPayment(orderId, paymentDetails);
     return ResponseEntity.ok().build();
 }
}
