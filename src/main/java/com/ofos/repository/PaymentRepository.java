package com.ofos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ofos.entity.Payment;
import com.ofos.entity.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByOrderId(Long orderId);
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    List<Payment> findByPaymentStatus(PaymentStatus status);
    
    @Modifying
    @Transactional
    @Query("UPDATE Payment p SET p.paymentStatus = :status WHERE p.id = :paymentId")
    void updatePaymentStatus(@Param("paymentId") Long paymentId, @Param("status") PaymentStatus status);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentStatus = 'PENDING' AND p.createdAt < :timeout")
    List<Payment> findPendingPaymentsTimeout(@Param("timeout") java.time.LocalDateTime timeout);
}
