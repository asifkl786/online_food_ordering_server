package com.ofos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "restaurants")
@Data
public class Restaurant extends BaseEntity {
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    private String logoUrl;
    
    private String coverImageUrl;
    
    @Column(nullable = false)
    private String cuisineType;
    
    private BigDecimal minimumOrderAmount;
    
    private BigDecimal deliveryFee;
    
    private LocalTime openingTime;
    
    private LocalTime closingTime;
    
    @Column(nullable = false)
    private Boolean isOpen = true;
    
    private Double averageRating = 0.0;
    
    private Integer totalReviews = 0;
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems = new ArrayList<>();
    
    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders = new ArrayList<>();
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantAddress> addresses = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}