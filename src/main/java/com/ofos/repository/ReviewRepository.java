package com.ofos.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ofos.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    Page<Review> findByRestaurantId(Long restaurantId, Pageable pageable);
    
    List<Review> findByUserId(Long userId);
    
    boolean existsByUserIdAndOrderId(Long userId, Long orderId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.restaurant.id = :restaurantId")
    Double getAverageRatingForRestaurant(@Param("restaurantId") Long restaurantId);
    
    @Modifying
    @Transactional
    @Query("UPDATE Review r SET r.isVerified = true WHERE r.id = :reviewId")
    void verifyReview(@Param("reviewId") Long reviewId);
    
    @Query("SELECT r FROM Review r WHERE r.restaurant.id = :restaurantId AND r.rating >= :minRating")
    Page<Review> findReviewsByMinRating(@Param("restaurantId") Long restaurantId, 
                                        @Param("minRating") Integer minRating, 
                                        Pageable pageable);
}
