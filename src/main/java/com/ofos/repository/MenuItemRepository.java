package com.ofos.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ofos.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    List<MenuItem> findByRestaurantId(Long restaurantId);
    
    Page<MenuItem> findByRestaurantIdAndIsAvailableTrue(Long restaurantId, Pageable pageable);
    
    List<MenuItem> findByRestaurantIdAndIsVegetarianTrue(Long restaurantId);
    
    Page<MenuItem> findByRestaurantIdAndCategoryId(Long restaurantId, Long categoryId, Pageable pageable);
    
    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.price BETWEEN :minPrice AND :maxPrice")
    List<MenuItem> findMenuItemsByPriceRange(@Param("restaurantId") Long restaurantId,
                                              @Param("minPrice") BigDecimal minPrice,
                                              @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<MenuItem> searchMenuItems(@Param("restaurantId") Long restaurantId, @Param("keyword") String keyword);
    
    @Modifying
    @Transactional
    @Query("UPDATE MenuItem m SET m.isAvailable = :isAvailable WHERE m.id = :menuItemId")
    void updateAvailability(@Param("menuItemId") Long menuItemId, @Param("isAvailable") Boolean isAvailable);
    
    @Modifying
    @Transactional
    @Query("UPDATE MenuItem m SET m.price = :newPrice WHERE m.id = :menuItemId")
    void updatePrice(@Param("menuItemId") Long menuItemId, @Param("newPrice") BigDecimal newPrice);
    
    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId ORDER BY m.createdAt DESC")
    List<MenuItem> findRecentMenuItems(@Param("restaurantId") Long restaurantId, Pageable pageable);
    
    @Query("SELECT COUNT(m) FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.isAvailable = true")
    Long countAvailableMenuItems(@Param("restaurantId") Long restaurantId);
    
    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id IN :restaurantIds AND m.isAvailable = true")
    List<MenuItem> findMenuItemsByRestaurantIds(@Param("restaurantIds") List<Long> restaurantIds);
}
