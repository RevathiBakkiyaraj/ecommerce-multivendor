package com.rev.service.impl;

import com.rev.modal.CartItem;
import com.rev.modal.Cart;
import com.rev.modal.Product;
import com.rev.modal.User;
import com.rev.repository.CartItemRepository;
import com.rev.repository.CartRepository;
import com.rev.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCardItem(
            User user,
            Product product,
            String size,
            int quantity
    ) {

        Cart cart = findUserCart(user);

        CartItem isPresent =
                cartItemRepository.findByCartAndProductAndSize(
                        cart,
                        product,
                        size
                );

        if (isPresent == null) {

            CartItem cartItem = new CartItem();

            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);

            int totalPrice =
                    quantity * product.getSellingPrice();

            cartItem.setSellingPrice(totalPrice);
            cartItem.setMrpPrice(
                    quantity * product.getMrpPrice()
            );

            cart.getCartItems().add(cartItem);

            cartItem.setCart(cart);

            return cartItemRepository.save(cartItem);
        }

        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) {

        Cart cart = cartRepository.findByUserId(user.getId());

        // Create cart if user doesn't have one
        if (cart == null) {

            cart = new Cart();

            cart.setUser(user);

            cart.setCartItems(new HashSet<>());

            cart = cartRepository.save(cart);
        }

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {

            totalPrice += cartItem.getMrpPrice();

            totalDiscountedPrice +=
                    cartItem.getSellingPrice();

            totalItem += cartItem.getQuantity();
        }

        cart.setTotalMrpPrice(totalPrice);

        cart.setTotalItem(totalItem);

        cart.setTotalSellingPrice(
                totalDiscountedPrice
        );

        cart.setDiscount(
                calculateDiscountPercentage(
                        totalPrice,
                        totalDiscountedPrice
                )
        );

        return cart;
    }

    private int calculateDiscountPercentage(
            int mrpPrice,
            int sellingPrice
    ) {

        if (mrpPrice <= 0) {
            return 0;
        }

        double discount =
                mrpPrice - sellingPrice;

        double discountPercentage =
                (discount / mrpPrice) * 100;

        return (int) discountPercentage;
    }
}

