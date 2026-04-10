package com.ofos.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ofos.dto.request.OrderItemRequest;
import com.ofos.dto.request.OrderRequest;
import com.ofos.dto.response.AddressResponse;
import com.ofos.dto.response.DeliveryInfoResponse;
import com.ofos.dto.response.OrderItemResponse;
import com.ofos.dto.response.OrderResponse;
import com.ofos.dto.response.RestaurantInfoResponse;
import com.ofos.dto.response.UserInfoResponse;
import com.ofos.entity.Address;
import com.ofos.entity.MenuItem;
import com.ofos.entity.OrderItem;
import com.ofos.entity.OrderStatus;
import com.ofos.entity.OrderTracking;
import com.ofos.entity.PaymentStatus;
import com.ofos.entity.Restaurant;
import com.ofos.entity.Order;
import com.ofos.entity.User;
import com.ofos.exception.BusinessException;
import com.ofos.exception.ResourceNotFoundException;
import com.ofos.repository.AddressRepository;
import com.ofos.repository.CartRepository;
import com.ofos.repository.MenuItemRepository;
import com.ofos.repository.OrderRepository;
import com.ofos.repository.RestaurantRepository;
import com.ofos.repository.UserRepository;
import com.ofos.service.OrderService;
import com.ofos.utils.OrderNumberGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
 
 private final OrderRepository orderRepository;
 private final UserRepository userRepository;
 private final RestaurantRepository restaurantRepository;
 private final MenuItemRepository menuItemRepository;
 private final CartRepository cartRepository;
 private final AddressRepository addressRepository;
 private final OrderNumberGenerator orderNumberGenerator;
 private final ModelMapper modelMapper;
 
 private static final BigDecimal TAX_RATE = new BigDecimal("0.10");
 
 @Override
 @Transactional
 public OrderResponse createOrder(OrderRequest request, String userEmail) {
     log.info("Creating order for user: {}", userEmail);
     
     // Get user
     User user = userRepository.findByEmail(userEmail)
             .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));
     
     // Get restaurant
     Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
             .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + request.getRestaurantId()));
     
     // Validate restaurant is open
     if (!restaurant.getIsOpen()) {
         log.warn("Restaurant is closed: {}", restaurant.getId());
         throw new BusinessException("Restaurant is currently closed");
     }
     
     // Get delivery address
     Address address = addressRepository.findByIdAndUser(request.getAddressId(), user)
             .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
     
     // Validate and prepare order items
     List<OrderItem> orderItems = prepareOrderItems(request.getItems(), restaurant);
     
     // Calculate order amounts
     BigDecimal subtotal = calculateSubtotal(orderItems);
     BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
     BigDecimal deliveryFee = restaurant.getDeliveryFee() != null ? restaurant.getDeliveryFee() : BigDecimal.ZERO;
     BigDecimal discount = calculateDiscount(request.getCouponCode(), subtotal);
     BigDecimal totalAmount = subtotal.add(tax).add(deliveryFee).subtract(discount);
     
     // Create order
     Order order = new Order();
     order.setOrderNumber(orderNumberGenerator.generateOrderNumber());
     order.setUser(user);
     order.setRestaurant(restaurant);
     order.setDeliveryAddress(address);
     order.setOrderItems(orderItems);
     order.setSubtotal(subtotal);
     order.setTax(tax);
     order.setDeliveryFee(deliveryFee);
     order.setDiscount(discount);
     order.setTotalAmount(totalAmount);
     order.setStatus(OrderStatus.PENDING);
     order.setPaymentStatus(PaymentStatus.PENDING);
     order.setSpecialInstructions(request.getSpecialInstructions());
     order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(45));
     
     // Set order reference in items
     orderItems.forEach(item -> item.setOrder(order));
     
     Order savedOrder = orderRepository.save(order);
     log.info("Order created successfully with number: {}", savedOrder.getOrderNumber());
     
     // Clear user's cart
     clearUserCart(user);
     
     return convertToResponse(savedOrder);
 }
 
 private List<OrderItem> prepareOrderItems(List<OrderItemRequest> itemRequests, Restaurant restaurant) {
     return itemRequests.stream().map(request -> {
         MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                 .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + request.getMenuItemId()));
         
         // Validate menu item belongs to restaurant
         if (!menuItem.getRestaurant().getId().equals(restaurant.getId())) {
             throw new BusinessException("Menu item does not belong to the specified restaurant");
         }
         
         // Validate availability
         if (!menuItem.getIsAvailable()) {
             throw new BusinessException("Menu item is not available: " + menuItem.getName());
         }
         
         OrderItem orderItem = new OrderItem();
         orderItem.setMenuItem(menuItem);
         orderItem.setQuantity(request.getQuantity());
         orderItem.setUnitPrice(menuItem.getPrice());
         orderItem.setSubtotal(menuItem.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
         orderItem.setItemName(menuItem.getName());
         
         return orderItem;
     }).collect(Collectors.toList());
 }
 
 private BigDecimal calculateSubtotal(List<OrderItem> orderItems) {
     return orderItems.stream()
             .map(OrderItem::getSubtotal)
             .reduce(BigDecimal.ZERO, BigDecimal::add);
 }
 
 private BigDecimal calculateDiscount(String couponCode, BigDecimal subtotal) {
     // Implement discount logic with coupon validation
     if (couponCode != null && "WELCOME10".equals(couponCode)) {
         return subtotal.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
     }
     return BigDecimal.ZERO;
 }
 
 private void clearUserCart(User user) {
     cartRepository.findByUser(user).ifPresent(cart -> {
         cart.getItems().clear();
         cart.setTotalAmount(BigDecimal.ZERO);
         cart.setTotalItems(0);
         cartRepository.save(cart);
         log.debug("Cleared cart for user: {}", user.getEmail());
     });
 }
 
 @Override
 @Transactional(readOnly = true)
 public OrderResponse getOrderById(Long orderId) {
     log.debug("Fetching order by id: {}", orderId);
     Order order = orderRepository.findById(orderId)
             .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
     return convertToResponse(order);
 }
 
 @Override
 @Transactional
 public OrderResponse updateOrderStatus(Long orderId, OrderStatus status, String userEmail) {
     log.info("Updating order status for order {} to {}", orderId, status);
     
     Order order = orderRepository.findById(orderId)
             .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
     
     // Validate status transition
     validateStatusTransition(order.getStatus(), status);
     
     order.setStatus(status);
     
     // Update tracking
     if (order.getTracking() == null) {
         OrderTracking tracking = new OrderTracking();
         tracking.setOrder(order);
         tracking.setCurrentStatus(status);
         tracking.setLastUpdateTime(LocalDateTime.now());
         order.setTracking(tracking);
     } else {
         order.getTracking().setCurrentStatus(status);
         order.getTracking().setLastUpdateTime(LocalDateTime.now());
     }
     
     order.getTracking().getStatusHistory().put(status, LocalDateTime.now());
     
     Order updatedOrder = orderRepository.save(order);
     log.info("Order status updated successfully for order: {}", updatedOrder.getOrderNumber());
     
     // Send real-time notification via WebSocket
     sendOrderStatusUpdate(updatedOrder);
     
     return convertToResponse(updatedOrder);
 }
 
 private void validateStatusTransition(OrderStatus current, OrderStatus next) {
     // Implement status transition validation logic
     log.debug("Validating status transition from {} to {}", current, next);
 }
 
 private void sendOrderStatusUpdate(Order order) {
     // Implement WebSocket notification
     log.info("Sending WebSocket notification for order: {}", order.getOrderNumber());
 }
 
 @Override
 @Transactional
 public OrderResponse cancelOrder(Long orderId, String userEmail) {
     log.info("Cancelling order: {} by user: {}", orderId, userEmail);
     
     Order order = orderRepository.findById(orderId)
             .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
     
     // Check if order can be cancelled
     if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
         throw new BusinessException("Order cannot be cancelled in current status: " + order.getStatus());
     }
     
     order.setStatus(OrderStatus.CANCELLED);
     order.setPaymentStatus(PaymentStatus.REFUNDED);
     
     Order cancelledOrder = orderRepository.save(order);
     log.info("Order cancelled successfully: {}", cancelledOrder.getOrderNumber());
     
     return convertToResponse(cancelledOrder);
 }
 
 @Override
 public void processPayment(Long orderId, String paymentDetails) {
     log.info("Processing payment for order: {}", orderId);
     // Implement payment gateway integration
 }
 
 @Override
 @Transactional(readOnly = true)
 public Page<OrderResponse> getUserOrders(String userEmail, Pageable pageable) {
     log.debug("Fetching orders for user: {}", userEmail);
     User user = userRepository.findByEmail(userEmail)
             .orElseThrow(() -> new ResourceNotFoundException("User not found"));
     
     return orderRepository.findByUser(user, pageable)
             .map(this::convertToResponse);
 }
 
 @Override
 public OrderResponse assignDeliveryPartner(Long orderId, Long deliveryPartnerId) {
     log.info("Assigning delivery partner {} to order {}", deliveryPartnerId, orderId);
     
     Order order = orderRepository.findById(orderId)
             .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
     
     User deliveryPartner = userRepository.findById(deliveryPartnerId)
             .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found"));
     
     order.setDeliveryPartner(deliveryPartner);
     order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
     
     Order updatedOrder = orderRepository.save(order);
     return convertToResponse(updatedOrder);
 }
 
 @Override
 public OrderResponse getOrderByNumber(String orderNumber) {
     log.debug("Fetching order by number: {}", orderNumber);
     Order order = orderRepository.findByOrderNumber(orderNumber)
             .orElseThrow(() -> new ResourceNotFoundException("Order not found with number: " + orderNumber));
     return convertToResponse(order);
 }
 
 private OrderResponse convertToResponse(Order order) {
	    OrderResponse response = new OrderResponse();
	    
	    // Basic order details
	    response.setId(order.getId());
	    response.setOrderNumber(order.getOrderNumber());
	    response.setStatus(order.getStatus().toString());
	    response.setPaymentStatus(order.getPaymentStatus().toString());
	    response.setSubtotal(order.getSubtotal());
	    response.setTax(order.getTax());
	    response.setDeliveryFee(order.getDeliveryFee());
	    response.setDiscount(order.getDiscount());
	    response.setTotalAmount(order.getTotalAmount());
	    response.setCreatedAt(order.getCreatedAt());
	    response.setUpdatedAt(order.getUpdatedAt());
	    response.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime());
	    response.setSpecialInstructions(order.getSpecialInstructions());
	    
	    // Map RestaurantInfoResponse
	    if (order.getRestaurant() != null) {
	        RestaurantInfoResponse restaurantInfo = new RestaurantInfoResponse();
	        restaurantInfo.setId(order.getRestaurant().getId());
	        restaurantInfo.setName(order.getRestaurant().getName());
	        restaurantInfo.setDescription(order.getRestaurant().getDescription());
	        restaurantInfo.setCuisineType(order.getRestaurant().getCuisineType());
	        restaurantInfo.setLogoUrl(order.getRestaurant().getLogoUrl());
	        restaurantInfo.setCoverImageUrl(order.getRestaurant().getCoverImageUrl());
	        restaurantInfo.setAverageRating(order.getRestaurant().getAverageRating());
	        restaurantInfo.setTotalReviews(order.getRestaurant().getTotalReviews());
	        restaurantInfo.setIsOpen(order.getRestaurant().getIsOpen());
	        restaurantInfo.setMinimumOrderAmount(order.getRestaurant().getMinimumOrderAmount());
	        restaurantInfo.setDeliveryFee(order.getRestaurant().getDeliveryFee());
	        restaurantInfo.setOpeningTime(order.getRestaurant().getOpeningTime() != null ? 
	            order.getRestaurant().getOpeningTime().toString() : null);
	        restaurantInfo.setClosingTime(order.getRestaurant().getClosingTime() != null ? 
	            order.getRestaurant().getClosingTime().toString() : null);
	        response.setRestaurant(restaurantInfo);
	    }
	    
	    // Map UserInfoResponse
	    if (order.getUser() != null) {
	        UserInfoResponse userInfo = new UserInfoResponse();
	        userInfo.setId(order.getUser().getId());
	        userInfo.setEmail(order.getUser().getEmail());
	        userInfo.setFirstName(order.getUser().getFirstName());
	        userInfo.setLastName(order.getUser().getLastName());
	        userInfo.setPhoneNumber(order.getUser().getPhoneNumber());
	        userInfo.setProfileImageUrl(order.getUser().getProfileImageUrl());
	        userInfo.setRole(order.getUser().getRole() != null ? order.getUser().getRole().toString() : null);
	        userInfo.setIsActive(order.getUser().getIsActive());
	        response.setUser(userInfo);
	    }
	    
	    // Map DeliveryInfoResponse
	    if (order.getDeliveryPartner() != null) {
	        DeliveryInfoResponse deliveryInfo = new DeliveryInfoResponse();
	        deliveryInfo.setDeliveryPartnerId(order.getDeliveryPartner().getId());
	        deliveryInfo.setDeliveryPartnerName(order.getDeliveryPartner().getFirstName() + " " + order.getDeliveryPartner().getLastName());
	        deliveryInfo.setDeliveryPartnerPhone(order.getDeliveryPartner().getPhoneNumber());
	        deliveryInfo.setDeliveryStatus(order.getStatus().toString());
	        
	        if (order.getTracking() != null) {
	            deliveryInfo.setCurrentLatitude(order.getTracking().getCurrentLatitude());
	            deliveryInfo.setCurrentLongitude(order.getTracking().getCurrentLongitude());
	            deliveryInfo.setLastUpdateTime(order.getTracking().getLastUpdateTime() != null ? 
	                order.getTracking().getLastUpdateTime().toString() : null);
	        }
	        
	        if (order.getEstimatedDeliveryTime() != null) {
	            deliveryInfo.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime().toString());
	        }
	        
	        response.setDeliveryInfo(deliveryInfo);
	    }
	    
	    // Map AddressResponse
	    if (order.getDeliveryAddress() != null) {
	        AddressResponse addressResponse = new AddressResponse();
	        addressResponse.setId(order.getDeliveryAddress().getId());
	        addressResponse.setStreetAddress(order.getDeliveryAddress().getStreetAddress());
	        addressResponse.setApartmentNumber(order.getDeliveryAddress().getApartmentNumber());
	        addressResponse.setCity(order.getDeliveryAddress().getCity());
	        addressResponse.setState(order.getDeliveryAddress().getState());
	        addressResponse.setZipCode(order.getDeliveryAddress().getZipCode());
	        addressResponse.setCountry(order.getDeliveryAddress().getCountry());
	        addressResponse.setLandmark(order.getDeliveryAddress().getLandmark());
	        addressResponse.setAddressType(order.getDeliveryAddress().getAddressType());
	        addressResponse.setLatitude(order.getDeliveryAddress().getLatitude());
	        addressResponse.setLongitude(order.getDeliveryAddress().getLongitude());
	        addressResponse.setIsDefault(order.getDeliveryAddress().getIsDefault());
	        
	        if (order.getUser() != null) {
	            addressResponse.setUserId(order.getUser().getId());
	        }
	        
	        response.setDeliveryAddress(addressResponse);
	    }
	    
	    // Map Order Items
	    if (order.getOrderItems() != null) {
	        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
	                .map(item -> {
	                    OrderItemResponse itemResponse = new OrderItemResponse();
	                   // itemResponse.setId(item.getId());
	                    itemResponse.setQuantity(item.getQuantity());
	                    itemResponse.setUnitPrice(item.getUnitPrice());
	                    itemResponse.setSubtotal(item.getSubtotal());
	                    itemResponse.setItemName(item.getItemName());
	                   // itemResponse.setSpecialInstructions(item.getSpecialInstructions());
	                    
	                    if (item.getMenuItem() != null) {
	                        itemResponse.setMenuItemId(item.getMenuItem().getId());
	                    }
	                    
	                    return itemResponse;
	                }).collect(Collectors.toList());
	        
	        response.setItems(itemResponses);
	    }
	    
	    return response;
	}
}