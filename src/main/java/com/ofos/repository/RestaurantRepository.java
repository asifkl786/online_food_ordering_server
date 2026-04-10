package com.ofos.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ofos.entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    
    Optional<Restaurant> findByName(String name);
    
    Page<Restaurant> findByCuisineType(String cuisineType, Pageable pageable);
    
    Page<Restaurant> findByIsOpenTrue(Pageable pageable);
    
    List<Restaurant> findByOwnerId(Long ownerId);
    
    @Query("SELECT r FROM Restaurant r WHERE r.isOpen = true AND r.averageRating >= :minRating")
    Page<Restaurant> findOpenRestaurantsWithMinRating(@Param("minRating") Double minRating, Pageable pageable);
    
    @Query("SELECT r FROM Restaurant r WHERE " +
           "LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.cuisineType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Restaurant> searchRestaurants(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT r FROM Restaurant r ORDER BY r.averageRating DESC")
    Page<Restaurant> findTopRatedRestaurants(Pageable pageable);
    
    @Query("SELECT r FROM Restaurant r WHERE r.minimumOrderAmount <= :amount AND r.isOpen = true")
    List<Restaurant> findRestaurantsWithMinOrderAmountLessThanEqual(@Param("amount") BigDecimal amount);
    
    @Modifying
    @Transactional
    @Query("UPDATE Restaurant r SET r.isOpen = :isOpen WHERE r.id = :restaurantId")
    void updateRestaurantStatus(@Param("restaurantId") Long restaurantId, @Param("isOpen") Boolean isOpen);
    
    @Modifying
    @Transactional
    @Query("UPDATE Restaurant r SET r.averageRating = :avgRating, r.totalReviews = :totalReviews WHERE r.id = :restaurantId")
    void updateRestaurantRating(@Param("restaurantId") Long restaurantId, 
                                @Param("avgRating") Double avgRating, 
                                @Param("totalReviews") Integer totalReviews);
    
    @Query("SELECT COUNT(o) FROM Restaurant r JOIN r.orders o WHERE r.id = :restaurantId AND o.status = 'DELIVERED'")
    Long countCompletedOrders(@Param("restaurantId") Long restaurantId);
    
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Restaurant r JOIN r.orders o WHERE r.id = :restaurantId AND o.status = 'DELIVERED'")
    BigDecimal calculateTotalRevenue(@Param("restaurantId") Long restaurantId);
}
