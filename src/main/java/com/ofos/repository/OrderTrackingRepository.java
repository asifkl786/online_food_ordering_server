package com.ofos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ofos.entity.OrderStatus;
import com.ofos.entity.OrderTracking;

@Repository
public interface OrderTrackingRepository extends JpaRepository<OrderTracking, Long> {
    
    Optional<OrderTracking> findByOrderId(Long orderId);
    
    @Modifying
    @Transactional
    @Query("UPDATE OrderTracking ot SET ot.currentLatitude = :latitude, ot.currentLongitude = :longitude, ot.lastUpdateTime = CURRENT_TIMESTAMP WHERE ot.order.id = :orderId")
    void updateDeliveryLocation(@Param("orderId") Long orderId, 
                                @Param("latitude") Double latitude, 
                                @Param("longitude") Double longitude);
    
    @Query("SELECT ot FROM OrderTracking ot WHERE ot.currentStatus = :status")
    java.util.List<OrderTracking> findByCurrentStatus(@Param("status") OrderStatus status);
}
