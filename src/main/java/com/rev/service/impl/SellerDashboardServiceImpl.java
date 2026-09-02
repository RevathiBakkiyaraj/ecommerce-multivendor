package com.rev.service.impl;

import com.rev.domain.OrderStatus;
import com.rev.modal.Order;
import com.rev.modal.OrderItem;
import com.rev.modal.Product;
import com.rev.repository.OrderItemRepository;
import com.rev.repository.OrderRepository;
import com.rev.repository.ProductRepository;
import com.rev.response.SellerDashboardResponse;
import com.rev.service.SellerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerDashboardServiceImpl implements SellerDashboardService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public SellerDashboardResponse getDashboard(Long sellerId) {

        // -----------------------------
        // PRODUCTS
        // -----------------------------

        List<Product> products =
                productRepository.findBySellerId(sellerId);

        Long totalProducts = (long) products.size();


        // -----------------------------
        // ORDERS
        // -----------------------------

        List<Order> orders =
                orderRepository.findBySellerId(sellerId);

        Long totalOrders = (long) orders.size();


        // -----------------------------
        // TOTAL SALES
        // -----------------------------

        double totalSales = orders.stream()
                .filter(order ->
                        order.getOrderStatus() != OrderStatus.CANCELLED)
                .mapToDouble(order ->
                        order.getTotalSellingPrice() != null
                                ? order.getTotalSellingPrice()
                                : 0)
                .sum();


        // -----------------------------
        // EARNINGS
        // -----------------------------

        double earnings = orders.stream()
                .filter(order ->
                        order.getOrderStatus() != OrderStatus.CANCELLED)
                .mapToDouble(order ->
                        order.getTotalSellingPrice() != null
                                ? order.getTotalSellingPrice()
                                : 0)
                .sum();


        // -----------------------------
        // LOW STOCK
        // -----------------------------

        List<SellerDashboardResponse.LowStockProduct>
                lowStockProducts = products.stream()
                .filter(product ->
                        product.getQuantity() <= 5)
                .map(product ->
                        new SellerDashboardResponse.LowStockProduct(
                                product.getId(),
                                product.getTitle(),
                                product.getQuantity()
                        ))
                .collect(Collectors.toList());


        // -----------------------------
        // RECENT ORDERS
        // -----------------------------

        List<SellerDashboardResponse.RecentOrder>
                recentOrders = orders.stream()
                .sorted(Comparator.comparing(
                        Order::getOrderDate,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()
                        )))
                .limit(5)
                .map(order ->
                        new SellerDashboardResponse.RecentOrder(
                                order.getId(),
                                order.getUser() != null
                                        ? order.getUser().getFullName()
                                        : "Customer",
                                order.getTotalSellingPrice() != null
                                        ? order.getTotalSellingPrice().doubleValue()
                                        : 0.0,
                                order.getOrderStatus() != null
                                        ? order.getOrderStatus().name()
                                        : "PENDING"
                        ))
                .collect(Collectors.toList());


        // -----------------------------
        // TOP SELLING PRODUCTS
        // -----------------------------

        Map<Long, List<OrderItem>> groupedItems =
                orders.stream()
                        .flatMap(order ->
                                order.getOrderItems().stream())
                        .filter(item ->
                                item.getProduct() != null)
                        .collect(Collectors.groupingBy(
                                item -> item.getProduct().getId()
                        ));


        List<SellerDashboardResponse.TopSellingProduct>
                topSellingProducts =
                groupedItems.entrySet()
                        .stream()
                        .map(entry -> {

                            Product product =
                                    entry.getValue()
                                            .get(0)
                                            .getProduct();

                            long unitsSold =
                                    entry.getValue()
                                            .stream()
                                            .mapToLong(
                                                    OrderItem::getQuantity)
                                            .sum();

                            double revenue =
                                    entry.getValue()
                                            .stream()
                                            .mapToDouble(item ->
                                                    item.getSellingPrice() != null
                                                            ? item.getSellingPrice()
                                                            : 0)
                                            .sum();

                            return new SellerDashboardResponse
                                    .TopSellingProduct(
                                    product.getId(),
                                    product.getTitle(),
                                    unitsSold,
                                    revenue
                            );
                        })
                        .sorted(Comparator.comparing(
                                        SellerDashboardResponse
                                                .TopSellingProduct::getUnitsSold)
                                .reversed())
                        .limit(5)
                        .collect(Collectors.toList());


        // -----------------------------
        // SALES OVERVIEW
        // -----------------------------

        List<SellerDashboardResponse.SalesData>
                salesOverview = createSalesOverview(orders);


        // -----------------------------
        // RESPONSE
        // -----------------------------

        return new SellerDashboardResponse(
                totalSales,
                totalOrders,
                totalProducts,
                earnings,
                salesOverview,
                lowStockProducts,
                recentOrders,
                topSellingProducts
        );
    }


    private List<SellerDashboardResponse.SalesData>
    createSalesOverview(List<Order> orders) {

        Map<DayOfWeek, Double> salesByDay =
                new EnumMap<>(DayOfWeek.class);

        for (DayOfWeek day : DayOfWeek.values()) {
            salesByDay.put(day, 0.0);
        }

        LocalDate today = LocalDate.now();

        LocalDate startOfWeek =
                today.with(
                        java.time.temporal.TemporalAdjusters
                                .previousOrSame(DayOfWeek.MONDAY)
                );

        for (Order order : orders) {

            if (order.getOrderDate() == null) {
                continue;
            }

            LocalDate orderDate =
                    order.getOrderDate().toLocalDate();

            if (!orderDate.isBefore(startOfWeek)
                    && !orderDate.isAfter(today)
                    && order.getOrderStatus()
                    != OrderStatus.CANCELLED) {

                DayOfWeek day =
                        orderDate.getDayOfWeek();

                double amount =
                        order.getTotalSellingPrice() != null
                                ? order.getTotalSellingPrice()
                                : 0;

                salesByDay.put(
                        day,
                        salesByDay.get(day) + amount
                );
            }
        }

        List<SellerDashboardResponse.SalesData>
                result = new ArrayList<>();

        for (DayOfWeek day : DayOfWeek.values()) {

            String dayName =
                    day.name().substring(0, 1)
                            + day.name().substring(1, 3)
                            .toLowerCase();

            result.add(
                    new SellerDashboardResponse.SalesData(
                            dayName,
                            salesByDay.get(day)
                    )
            );
        }

        return result;
    }
}