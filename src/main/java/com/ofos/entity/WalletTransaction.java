package com.ofos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "wallet_transactions")
@Data
public class WalletTransaction extends BaseEntity {
 
	 @Column(unique = true)
	 private String transactionReference;
	 
	 @Column(nullable = false, precision = 10, scale = 2)
	 private BigDecimal amount;
	 
	 @Enumerated(EnumType.STRING)
	 private TransactionType transactionType;
	 
	 @Enumerated(EnumType.STRING)
	 private TransactionStatus status;
	 
	 private String description;
	 
	 private LocalDateTime transactionDate;
	 
	 @ManyToOne
	 @JoinColumn(name = "wallet_id", nullable = false)
	 private Wallet wallet;
	 
	 @ManyToOne
	 @JoinColumn(name = "order_id")
	 private Order order;
}

