package com.rev.service.impl;

import com.rev.modal.Product;
import com.rev.modal.Review;
import com.rev.modal.User;
import com.rev.modal.ReviewImage;
import java.util.ArrayList;
import com.rev.repository.ReviewRepository;
import com.rev.request.CreateReviewRequest;
import com.rev.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        List<ReviewImage> reviewImages = new ArrayList<>();

        for (String imageUrl : req.getProductImages()) {
            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setImageUrl(imageUrl);
            reviewImage.setReview(review);
            reviewImages.add(reviewImage);
        }

        review.setProductImages(reviewImages);

        product.getReviews().add(review);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception {
        Review review=getReviewById(reviewId);
        if(review.getUser().getId().equals(userId)) {
            review.setReviewText(reviewText);
            return reviewRepository.save(review);
        }
        throw new Exception("you can't update this review");
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception{
        Review review=getReviewById(reviewId);
        if(review.getUser().getId().equals(userId)) {
            throw new Exception("you can't delete this review");
        }
        reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long reviewId) throws Exception {
        return reviewRepository.findById(reviewId).orElseThrow(()->
                new Exception("review not found"));
    }
}
