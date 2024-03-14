package com.emi.onlineshop.services;

import com.emi.onlineshop.dtos.OrderDetailsResponse;
import com.emi.onlineshop.dtos.OrderResponse;
import com.emi.onlineshop.models.*;
import com.emi.onlineshop.repositories.CartItemRepository;
import com.emi.onlineshop.repositories.OrderRepository;
import com.emi.onlineshop.utils.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final AuthenticationService authenticationService;
    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;
    private final CartItemRepository cartItemRepository;
    private final Mapper mapper;
    private final InventoryService inventoryService;

    public OrderService(AuthenticationService authenticationService, OrderRepository orderRepository, CartItemService cartItemService,
                        CartItemRepository cartItemRepository, Mapper mapper, InventoryService inventoryService) {
        this.authenticationService = authenticationService;
        this.orderRepository = orderRepository;
        this.cartItemService = cartItemService;
        this.cartItemRepository = cartItemRepository;
        this.mapper = mapper;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public String createOrder() {
        User user = authenticationService.getAuthenticatedUser();

        List<CartItem> cartItems = cartItemService.getCartForUser();
        if (cartItems.isEmpty()) {
            return "Cart is empty.";
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotal(calculateTotal(cartItems));
        order.setOrderStatus(OrderStatus.PENDING);
        cartItems.forEach(cartItem -> order.addOrderDetail(getOrderDetail(cartItem)));

        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        return "Order with id " + savedOrder.getId() + " placed.";
    }

    public String deleteOrderById(long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return "done";
        }

        return "Can not find order with id: " + id;
    }

    public List<OrderResponse> findOrdersByUserId() {
        return orderRepository.findByUser_Email(authenticationService.getAuthenticatedUser().getEmail())
                .stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getLocalDate(),
                order.getTotal(),
                order.getOrderStatus(),
                mapper.mapUserResponse(order.getUser()),
                order.getOrderDetails()
                        .stream().map(this::mapToOrderDetailsResponse)
                        .collect(Collectors.toSet())
        );
    }

    private OrderDetailsResponse mapToOrderDetailsResponse(OrderDetail orderDetails) {
        return new OrderDetailsResponse(
                orderDetails.getQuantity(),
                orderDetails.getUnitPrice(),
                orderDetails.getSubtotal(),
                mapper.mapProductResponse(orderDetails.getProduct())
        );
    }

    private OrderDetail getOrderDetail(CartItem cartItem) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(cartItem.getProduct());
        orderDetail.setQuantity(cartItem.getQuantity());
        orderDetail.setSubtotal(cartItem.getSubtotal());
        orderDetail.setUnitPrice(cartItem.getProduct().getPrice());

        if (!inventoryService.isStockForProductSuccessfullyModified(cartItem.getProduct().getCode(), cartItem.getQuantity())) {
            throw new IllegalArgumentException("There aren't enough products: " + cartItem.getProduct().getCode());
        }

        return orderDetail;
    }

    private Double calculateTotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce((Double::sum))
                .get();
    }

}
