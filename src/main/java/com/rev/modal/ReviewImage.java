package com.rev.modal;

import jakarta.persistence.*;
import lombok.*;

    @Entity
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class ReviewImage {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String imageUrl;

        @ManyToOne
        @JoinColumn(name = "review_id")
        private Review review;
    }

