package com.rev.service;

import com.rev.response.SellerDashboardResponse;

public interface SellerDashboardService {

    SellerDashboardResponse getDashboard(Long sellerId);

}