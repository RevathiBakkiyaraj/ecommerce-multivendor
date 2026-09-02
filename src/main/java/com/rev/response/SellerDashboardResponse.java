package com.rev.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerDashboardResponse {

    private Double totalSales;
    private Long totalOrders;
    private Long totalProducts;
    private Double earnings;

    private List<SalesData> salesOverview;
    private List<LowStockProduct> lowStockProducts;
    private List<RecentOrder> recentOrders;
    private List<TopSellingProduct> topSellingProducts;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SalesData {
        private String day;
        private Double sales;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LowStockProduct {
        private Long id;
        private String name;
        private Integer stock;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecentOrder {
        private Long id;
        private String customer;
        private Double amount;
        private String status;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TopSellingProduct {
        private Long id;
        private String name;
        private Long unitsSold;
        private Double revenue;
    }
}