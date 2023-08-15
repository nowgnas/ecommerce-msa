package com.example.orders.service;

import com.example.orders.dto.OrderDto;
import com.example.orders.jpa.OrderRepository;
import com.example.orders.jpa.OrdersEntity;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private ModelMapper getMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());
        ModelMapper mapper = getMapper();
        OrdersEntity ordersEntity = mapper.map(orderDto, OrdersEntity.class);
        orderRepository.save(ordersEntity);
        return mapper.map(ordersEntity, OrderDto.class);
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        OrdersEntity byOrderId = orderRepository.findByOrderId(orderId);
        ModelMapper mapper = getMapper();
        return mapper.map(byOrderId, OrderDto.class);
    }

    @Override
    public Iterable<OrdersEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
