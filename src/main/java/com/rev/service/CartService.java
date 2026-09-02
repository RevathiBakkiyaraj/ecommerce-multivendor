package com.rev.service;

import com.rev.modal.CartItem;
import com.rev.modal.Cart;
import com.rev.modal.Product;
import com.rev.modal.User;

public interface CartService {

    public CartItem addCardItem(
            User user,
            Product product,
            String size,
            int quantity
    );
    public Cart findUserCart(User user);
}
