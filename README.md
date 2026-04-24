<img width="1536" height="1024" alt="OFOS" src="https://github.com/user-attachments/assets/f2657e9b-295b-4a40-9113-a6fa6733603f" />


<img width="448" height="249" alt="Structure" src="https://github.com/user-attachments/assets/9d9a5229-28e6-4023-954f-51119d355cda" />
<img width="318" height="211" alt="Project Technology" src="https://github.com/user-attachments/assets/c9b264b9-8dc7-45d5-a407-293ff33fdf98" />


# Online Food Ordering System - Complete Architecture

## Module wise Component Mapping Chart

| Module | Entity | Service Interface | Service Implementation | Repository | Request DTO | Response DTO | Controller |
|--------|--------|-------------------|------------------------|------------|-------------|--------------|------------|
| **User Management** | User, UserRole | UserService | UserServiceImpl | UserRepository | LoginRequest, UserRegistrationRequest | UserResponse, LoginResponse | UserController |
| **Restaurant Management** | Restaurant, RestaurantAddress | RestaurantService | RestaurantServiceImpl | RestaurantRepository | RestaurantRequest | RestaurantResponse, RestaurantInfoResponse | RestaurantController |
| **Menu Management** | MenuItem, Category | MenuItemService | MenuItemServiceImpl | MenuItemRepository, CategoryRepository | MenuItemRequest | MenuItemResponse | MenuController |
| **Cart Management** | Cart, CartItem | CartService | CartServiceImpl | CartRepository, CartItemRepository | CartItemRequest | CartResponse, CartItemResponse | CartController |
| **Order Management** | Order, OrderItem, OrderStatus, PaymentStatus | OrderService | OrderServiceImpl | OrderRepository, OrderItemRepository | OrderRequest, OrderItemRequest | OrderResponse, OrderItemResponse | OrderController |
| **Address Management** | Address | AddressService | AddressServiceImpl | AddressRepository | AddressRequest | AddressResponse | AddressController |
| **Payment Processing** | Payment, PaymentMethod | PaymentService | PaymentServiceImpl | PaymentRepository | PaymentRequest | PaymentResponse | PaymentController |
| **Order Tracking** | OrderTracking | TrackingService | TrackingServiceImpl | OrderTrackingRepository | LocationUpdateRequest | TrackingResponse | TrackingController |
| **Review & Rating** | Review | ReviewService | ReviewServiceImpl | ReviewRepository | ReviewRequest | ReviewResponse | ReviewController |
| **Wallet Management** | Wallet, WalletTransaction, TransactionType, TransactionStatus | WalletService | WalletServiceImpl | WalletRepository | WalletRequest | WalletResponse, WalletTransactionResponse | WalletController |
| **Delivery Management** | User (as DeliveryPartner) | DeliveryService | DeliveryServiceImpl | UserRepository | DeliveryAssignmentRequest | DeliveryResponse, DeliveryInfoResponse | DeliveryController |
| **Notification** | - | NotificationService | NotificationServiceImpl | - | NotificationRequest | NotificationResponse | NotificationController |
| **Authentication & Authorization** | - | AuthService | AuthServiceImpl | UserRepository | LoginRequest, RegisterRequest | JwtResponse, AuthResponse | AuthController |
| **Coupon/Discount** | Coupon (if implemented) | CouponService | CouponServiceImpl | CouponRepository | CouponRequest | CouponResponse | CouponController |

## Detailed Component Breakdown

### 1. User Management Module



# 🍔 Online Food Ordering System - Enterprise Level Backend

## 📋 Project Overview
Complete enterprise-level online food ordering system with real-time tracking, payment integration, and delivery management.

## 🛠️ Technology Stack
- **Java 17** - Core language
- **Spring Boot 3.1.x** - Framework
- **Spring Security** - Authentication
- **Spring Data JPA** - Database operations
- **Spring WebSocket** - Real-time updates
- **PostgreSQL** - Primary database
- **Redis** - Caching & session management
- **RabbitMQ** - Message queue
- **JWT** - Token based authentication
- **ModelMapper** - DTO mapping
- **Lombok** - Boilerplate code reduction
- **Maven** - Build tool

## 📁 Project Structure

---

## 1. 🗄️ Entity Module (25 Files)

**Package:** `com.ofos.entity`

| # | File Name | Purpose |
|---|-----------|---------|
| 1 | `BaseEntity.java` | Abstract base class with common fields (id, createdAt, updatedAt) |
| 2 | `User.java` | User details, roles, authentication |
| 3 | `Address.java` | Delivery addresses for users |
| 4 | `Restaurant.java` | Restaurant information and settings |
| 5 | `RestaurantAddress.java` | Restaurant physical locations |
| 6 | `MenuItem.java` | Food items with price and availability |
| 7 | `Category.java` | Menu categories (Veg, Non-Veg, Beverages) |
| 8 | `Order.java` | Order details and status |
| 9 | `OrderItem.java` | Individual items in an order |
| 10 | `Cart.java` | User shopping cart |
| 11 | `CartItem.java` | Items inside cart |
| 12 | `Payment.java` | Payment transaction details |
| 13 | `OrderTracking.java` | Real-time order tracking |
| 14 | `Review.java` | User ratings and reviews |
| 15 | `Wallet.java` | User wallet for balance |
| 16 | `WalletTransaction.java` | Wallet transaction history |
| 17 | `UserRole.java` | Enum for user roles |
| 18 | `OrderStatus.java` | Enum for order states |
| 19 | `PaymentStatus.java` | Enum for payment states |
| 20 | `PaymentMethod.java` | Enum for payment methods |
| 21 | `TransactionType.java` | Enum for transaction types |
| 22 | `TransactionStatus.java` | Enum for transaction states |
| 23 | `DeliveryStatus.java` | Enum for delivery states |
| 24 | `Coupon.java` | Discount coupons |
| 25 | `Notification.java` | User notifications |

---

## 2. 📁 Repository Module (14 Files)

**Package:** `com.ofos.repository`

| # | File Name | Key Methods |
|---|-----------|-------------|
| 1 | `UserRepository.java` | `findByEmail()`, `findByRole()`, `existsByEmail()` |
| 2 | `OrderRepository.java` | `findByOrderNumber()`, `updateOrderStatus()`, `findByUser()` |
| 3 | `RestaurantRepository.java` | `findByCuisineType()`, `findTopRatedRestaurants()` |
| 4 | `MenuItemRepository.java` | `findByRestaurantId()`, `searchMenuItems()` |
| 5 | `AddressRepository.java` | `findByUser()`, `findDefaultAddress()` |
| 6 | `CartRepository.java` | `findByUser()`, `updateCartTotals()` |
| 7 | `CartItemRepository.java` | `findByCartId()`, `deleteAllByCartId()` |
| 8 | `PaymentRepository.java` | `findByOrderId()`, `updatePaymentStatus()` |
| 9 | `OrderTrackingRepository.java` | `findByOrderId()`, `updateDeliveryLocation()` |
| 10 | `ReviewRepository.java` | `findByRestaurantId()`, `getAverageRating()` |
| 11 | `WalletRepository.java` | `findByUserId()`, `addBalance()`, `deductBalance()` |
| 12 | `CategoryRepository.java` | `findByName()`, `findSubCategories()` |
| 13 | `OrderItemRepository.java` | `findByOrderId()`, `findTopSellingItems()` |
| 14 | `CouponRepository.java` | `findByCode()`, `validateCoupon()` |

---

## 3. ⚙️ Service Module (16 Files)

**Package:** `com.ofos.service` & `com.ofos.service.impl`

### Service Interfaces (8 files):

| # | File Name | Methods |
|---|-----------|---------|
| 1 | `UserService.java` | `registerUser()`, `loginUser()`, `updateProfile()`, `changePassword()` |
| 2 | `OrderService.java` | `createOrder()`, `cancelOrder()`, `updateOrderStatus()`, `getOrderById()` |
| 3 | `RestaurantService.java` | `addRestaurant()`, `updateMenu()`, `getNearbyRestaurants()`, `updateStatus()` |
| 4 | `CartService.java` | `addToCart()`, `removeFromCart()`, `updateQuantity()`, `clearCart()` |
| 5 | `PaymentService.java` | `processPayment()`, `refundPayment()`, `getPaymentStatus()` |
| 6 | `DeliveryService.java` | `assignDeliveryPartner()`, `updateLocation()`, `completeDelivery()` |
| 7 | `ReviewService.java` | `addReview()`, `updateReview()`, `deleteReview()`, `getRestaurantReviews()` |
| 8 | `ReportService.java` | `generateSalesReport()`, `getRevenueStats()`, `getOrderAnalytics()` |

### Service Implementations (8 files):

| # | File Name | Description |
|---|-----------|-------------|
| 1 | `UserServiceImpl.java` | User registration, login, profile management logic |
| 2 | `OrderServiceImpl.java` | Order creation, status updates, cancellation logic |
| 3 | `RestaurantServiceImpl.java` | Restaurant CRUD, menu management logic |
| 4 | `CartServiceImpl.java` | Shopping cart operations with price calculation |
| 5 | `PaymentServiceImpl.java` | Payment gateway integration (Razorpay/Stripe) |
| 6 | `DeliveryServiceImpl.java` | Partner assignment and tracking logic |
| 7 | `ReviewServiceImpl.java` | Rating calculation and review management |
| 8 | `ReportServiceImpl.java` | Analytics and reporting with data aggregation |

---

## 4. 🌐 Controller Module (9 Files)

**Package:** `com.ofos.controller`

| # | File Name | Base Path | Key Endpoints |
|---|-----------|-----------|---------------|
| 1 | `UserController.java` | `/api/v1/users` | `POST /register`, `POST /login`, `GET /profile` |
| 2 | `OrderController.java` | `/api/v1/orders` | `POST /`, `GET /{id}`, `PUT /{id}/status` |
| 3 | `RestaurantController.java` | `/api/v1/restaurants` | `POST /`, `GET /`, `GET /{id}/menu` |
| 4 | `CartController.java` | `/api/v1/cart` | `GET /`, `POST /add`, `DELETE /remove/{id}` |
| 5 | `PaymentController.java` | `/api/v1/payments` | `POST /process`, `GET /{id}/status` |
| 6 | `DeliveryController.java` | `/api/v1/delivery` | `GET /orders`, `PUT /location` |
| 7 | `ReviewController.java` | `/api/v1/reviews` | `POST /`, `GET /restaurant/{id}` |
| 8 | `AdminController.java` | `/api/v1/admin` | `GET /dashboard`, `GET /reports` |
| 9 | `WebSocketController.java` | `/ws` | WebSocket connection endpoint |

---

## 5. 📦 DTO Module (19 Files)

**Package:** `com.ofos.dto`

### Request DTOs (7 files):

| # | File Name | Fields |
|---|-----------|--------|
| 1 | `LoginRequest.java` | email, password |
| 2 | `RegisterRequest.java` | email, password, firstName, lastName, phoneNumber |
| 3 | `OrderRequest.java` | restaurantId, addressId, items, paymentMethod, couponCode |
| 4 | `OrderItemRequest.java` | menuItemId, quantity |
| 5 | `AddressRequest.java` | streetAddress, city, state, zipCode, addressType |
| 6 | `PaymentRequest.java` | orderId, amount, paymentMethod, cardDetails |
| 7 | `ReviewRequest.java` | restaurantId, orderId, rating, comment |

### Response DTOs (12 files):

| # | File Name | Fields |
|---|-----------|--------|
| 1 | `LoginResponse.java` | accessToken, tokenType, expiresIn, userInfo |
| 2 | `OrderResponse.java` | id, orderNumber, status, totalAmount, items, restaurant |
| 3 | `UserInfoResponse.java` | id, email, firstName, lastName, phoneNumber, role |
| 4 | `RestaurantInfoResponse.java` | id, name, cuisineType, rating, isOpen, deliveryFee |
| 5 | `AddressResponse.java` | id, streetAddress, city, state, zipCode, isDefault |
| 6 | `DeliveryInfoResponse.java` | partnerId, partnerName, partnerPhone, liveLocation |
| 7 | `CartResponse.java` | id, items, totalAmount, totalItems |
| 8 | `CartItemResponse.java` | id, menuItemId, itemName, quantity, unitPrice, subtotal |
| 9 | `PaymentResponse.java` | transactionId, amount, status, paymentDate |
| 10 | `ApiResponse.java` | success, message, data |
| 11 | `MenuItemResponse.java` | id, name, price, isAvailable, category, preparationTime |
| 12 | `ReportResponse.java` | totalOrders, totalRevenue, averageOrderValue, topItems |

---

## 6. ⚠️ Exception Module (5 Files)

**Package:** `com.ofos.exception`

| # | File Name | Purpose |
|---|-----------|---------|
| 1 | `GlobalExceptionHandler.java` | Global exception handling with @ControllerAdvice |
| 2 | `ResourceNotFoundException.java` | Custom exception for missing resources (404) |
| 3 | `BusinessException.java` | Custom exception for business rule violations (400) |
| 4 | `AuthenticationException.java` | Custom exception for auth failures (401) |
| 5 | `ErrorResponse.java` | Standard error response structure |

### Error Response Structure:
```json
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Order not found with id: 123",
    "path": "/api/v1/orders/123",
    "validationErrors": {}
}

