package com.rev.repository;

import com.rev.modal.Cart;
import com.rev.modal.CartItem;
import com.rev.modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
