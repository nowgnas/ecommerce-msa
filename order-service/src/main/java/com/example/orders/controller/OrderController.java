package com.example.orders.controller;

import com.example.orders.dto.OrderDto;
import com.example.orders.dto.RequestOrder;
import com.example.orders.jpa.OrdersEntity;
import com.example.orders.service.OrderService;
import com.example.orders.util.StrictMapper;
import com.example.orders.vo.ResponseOrder;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("order-service")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody RequestOrder order,
            @PathVariable("userId") String userId) {
        log.info(order.toString());
        ModelMapper mapper = StrictMapper.getStrictMapper();

        OrderDto orderDto = mapper.map(order, OrderDto.class);
        orderDto.setUserId(userId);
        orderService.createOrder(orderDto);

        ResponseOrder order1 = mapper.map(orderDto, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(order1); // 성곰 메세지 전달
    }

    @GetMapping("{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) {
        Iterable<OrdersEntity> ordersByUserId = orderService.getOrdersByUserId(userId);
        log.info(ordersByUserId.toString());
        List<ResponseOrder> list = new ArrayList<>();
        ordersByUserId.forEach(v -> {
            list.add(StrictMapper.getStrictMapper().map(v, ResponseOrder.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

}
