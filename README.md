<img width="448" height="249" alt="Structure" src="https://github.com/user-attachments/assets/9d9a5229-28e6-4023-954f-51119d355cda" />
<img width="318" height="211" alt="Project Technology" src="https://github.com/user-attachments/assets/c9b264b9-8dc7-45d5-a407-293ff33fdf98" />

# Online Food Ordering System

## Modules

### 1. Entity Module (25 files)
- BaseEntity, User, Address, Restaurant
- MenuItem, Order, Cart, Payment
- Review, Wallet, Category

### 2. Repository Module (12 files)
- UserRepository, OrderRepository
- RestaurantRepository, CartRepository
- PaymentRepository, AddressRepository

### 3. Service Module (16 files)
- UserService, OrderService
- RestaurantService, CartService
- PaymentService, DeliveryService

### 4. Controller Module (8 files)
- UserController, OrderController
- RestaurantController, CartController
- PaymentController, AdminController

### 5. DTO Module (17 files)
- Request DTOs: 5 files
- Response DTOs: 12 files

### 6. Exception Module (4 files)
- GlobalExceptionHandler
- ResourceNotFoundException
- BusinessException
- ErrorResponse

### 7. Configuration Module (6 files)
- ModelMapperConfig
- WebSocketConfig
- RedisConfig
- SecurityConfig
- RabbitMQConfig
- SwaggerConfig

### 8. Security Module (5 files)
- JwtTokenProvider
- JwtAuthenticationFilter
- CustomUserDetailsService
- SecurityConfig
- PasswordEncoderConfig

### 9. Utility Module (4 files)
- OrderNumberGenerator
- DateUtils
- AmountCalculator
- ValidationUtils

### 10. WebSocket Module (3 files)
- WebSocketConfig
- OrderStatusWebSocketHandler
- DeliveryLocationWebSocketHandler

### 11. Payment Module (3 files)
- PaymentGatewayService
- PaymentCallbackHandler
- RefundService

### 12. Notification Module (3 files)
- EmailService
- SmsService
- PushNotificationService

### 13. Async Module (2 files)
- AsyncConfig
- AsyncOrderProcessor

## Total Statistics
- Total Files: 108
- Total Lines: ~15,000
- Modules: 15

## Technology Stack
- Java 17
- Spring Boot 3.1.x
- PostgreSQL
- Redis
- RabbitMQ
- WebSocket
- JWT

## Deployment
