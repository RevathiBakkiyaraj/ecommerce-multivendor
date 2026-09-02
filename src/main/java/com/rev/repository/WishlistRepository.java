package com.rev.repository;

import com.rev.modal.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Wishlist findByUserId(Long userId);

}
