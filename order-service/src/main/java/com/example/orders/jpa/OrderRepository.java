package com.example.orders.jpa;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrdersEntity, Long> {

    OrdersEntity findByOrderId(String orderId);

    Iterable<OrdersEntity> findByUserId(String userId);

}
