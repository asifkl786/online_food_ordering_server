package com.ofos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "carts")
@Data
public class Cart extends BaseEntity {
 
 @OneToOne
 @JoinColumn(name = "user_id", unique = true)
 private User user;
 
 @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
 private List<CartItem> items = new ArrayList<>();
 
 private BigDecimal totalAmount = BigDecimal.ZERO;
 
 private Integer totalItems = 0;
}



