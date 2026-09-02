package com.rev.service;

import com.rev.modal.Product;
import com.rev.modal.User;
import com.rev.modal.Wishlist;

public interface WishlistService {

    Wishlist createWishlist(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishlist(User user, Product product);
}
