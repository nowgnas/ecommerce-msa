package com.example.orders.service;

import com.example.orders.dto.OrderDto;
import com.example.orders.jpa.OrdersEntity;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDetails);

    OrderDto getOrderByOrderId(String orderId);

    Iterable<OrdersEntity> getOrdersByUserId(String userId);
}
