package com.rev.service;

import com.rev.modal.CartItem;

public interface CartItemService {

    CartItem updateCartItem(Long userId, Long id, CartItem cardItem) throws Exception;
    void removeCartItem(Long userId, Long cartItemId) throws Exception;
    CartItem findCartItemById(Long id) throws Exception;
}
