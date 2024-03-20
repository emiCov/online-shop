package com.emi.onlineshop.services;

import com.emi.onlineshop.models.Order;
import com.emi.onlineshop.models.OrderStatus;
import com.emi.onlineshop.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckoutService {

    private final OrderService orderService;
    private final CartItemService cartItemService;
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    public CheckoutService(OrderService orderService, CartItemService cartItemService, PaymentService paymentService, OrderRepository orderRepository, InventoryService inventoryService) {
        this.orderService = orderService;
        this.cartItemService = cartItemService;
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public String placeOrder() {
        Order savedOrder = orderService.createOrder();
        cartItemService.deleteCartForUser();
        if (paymentService.executePayment(savedOrder)) {
            savedOrder.setOrderStatus(OrderStatus.COMPLETED);
            orderRepository.save(savedOrder);
            return "Payment was successful. Order is completed.";
        }

        savedOrder.getOrderDetails().forEach(orderDetail ->
                inventoryService.addQuantityForProduct(orderDetail.getProduct().getCode(), orderDetail.getQuantity()));
        savedOrder.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(savedOrder);
        return "Payment was not successful. Order is not completed.";
    }
}
