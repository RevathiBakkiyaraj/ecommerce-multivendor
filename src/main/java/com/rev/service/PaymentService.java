package com.rev.service;

import com.razorpay.PaymentLink;
import com.rev.modal.PaymentOrder;
import com.rev.modal.User;
import com.rev.modal.Order;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderById(Long orderId) throws Exception;
    PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception;
    Boolean ProceedPaymentOrder(PaymentOrder paymentOrder,String paymentId, String paymentLinkId) throws Exception;
    PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) throws Exception;
    String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
