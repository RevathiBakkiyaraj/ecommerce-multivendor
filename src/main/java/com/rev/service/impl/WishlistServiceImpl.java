package com.rev.service.impl;

import com.rev.modal.Product;
import com.rev.modal.User;
import com.rev.modal.Wishlist;
import com.rev.repository.WishlistRepository;
import com.rev.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Override
    public Wishlist createWishlist(User user) {
        Wishlist wishlist=new Wishlist();
        wishlist.setUser(user);
        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist getWishlistByUserId(User user) {
        Wishlist wishlist=wishlistRepository.findByUserId(user.getId());
        if(wishlist==null){
            wishlist=createWishlist(user);
        }
        return wishlist;
    }

    @Override
    public Wishlist addProductToWishlist(User user, Product product) {

        Wishlist wishlist = getWishlistByUserId(user);

        if (wishlist.getProducts() == null) {
            wishlist.setProducts(new HashSet<>());
        }

        if (wishlist.getProducts().contains(product)) {
            wishlist.getProducts().remove(product);
        } else {
            wishlist.getProducts().add(product);
        }

        return wishlistRepository.save(wishlist);
    }
}
