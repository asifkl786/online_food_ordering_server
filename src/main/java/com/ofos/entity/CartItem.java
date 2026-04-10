package com.ofos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cart_items")
@Data
public class CartItem extends BaseEntity {

private Integer quantity;

private BigDecimal unitPrice;

private BigDecimal subtotal;

@ManyToOne
@JoinColumn(name = "cart_id", nullable = false)
private Cart cart;

@ManyToOne
@JoinColumn(name = "menu_item_id", nullable = false)
private MenuItem menuItem;

}
