package com.rev.controller;

import com.rev.modal.Seller;
import com.rev.response.SellerDashboardResponse;
import com.rev.service.SellerDashboardService;
import com.rev.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/dashboard")
public class SellerDashboardController {

    private final SellerDashboardService sellerDashboardService;
    private final SellerService sellerService;


    @GetMapping
    public ResponseEntity<SellerDashboardResponse>
    getDashboard(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        Seller seller =
                sellerService.getSellerProfile(jwt);

        SellerDashboardResponse response =
                sellerDashboardService
                        .getDashboard(seller.getId());

        return ResponseEntity.ok(response);
    }
}
