package com.rev.repository;

import com.rev.modal.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findBySellerId(Long sellerId);
    long countBySellerId(Long sellerId);
    List<Order> findTop5BySellerIdOrderByOrderDateDesc(Long sellerId);
}
