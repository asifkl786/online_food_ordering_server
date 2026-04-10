package com.ofos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ofos.entity.Cart;
import com.ofos.entity.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    Optional<Cart> findByUser(User user);
    
    Optional<Cart> findByUserId(Long userId);
    
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.user.id = :userId")
    Optional<Cart> findCartWithItemsByUserId(@Param("userId") Long userId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.menuItem.id = :menuItemId")
    void removeItemFromCart(@Param("cartId") Long cartId, @Param("menuItemId") Long menuItemId);
    
    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.totalAmount = :totalAmount, c.totalItems = :totalItems WHERE c.id = :cartId")
    void updateCartTotals(@Param("cartId") Long cartId, 
                          @Param("totalAmount") java.math.BigDecimal totalAmount, 
                          @Param("totalItems") Integer totalItems);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId")
    void deleteCartByUserId(@Param("userId") Long userId);
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cart c WHERE c.user.id = :userId")
    boolean existsByUserId(@Param("userId") Long userId);
}
