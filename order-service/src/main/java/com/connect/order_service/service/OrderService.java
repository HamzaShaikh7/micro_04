package com.connect.order_service.service;

import com.connect.order_service.config.ClientConfig;
import com.connect.order_service.dto.OrderRequest;
import com.connect.order_service.dto.OrderResponse;
import com.connect.order_service.model.Order;
import com.connect.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private ClientConfig clientConfig;


    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackPlaceOrder")
    @TimeLimiter(name = "inventory", fallbackMethod = "fallbackPlaceOrder")
    @Retry(name = "inventory", fallbackMethod = "fallbackPlaceOrder")
    public CompletableFuture<String> placeOrder(OrderRequest orderRequest) {
        return CompletableFuture.supplyAsync(() -> {
            boolean isInStock = clientConfig.checkInventory(orderRequest.skuCode(), orderRequest.quantity());
            if (isInStock) {
                Order order = mapToOrder(orderRequest);
                orderRepository.save(order);
                return "Order placed.";
            } else {
                return "Product unavailable";
            }
        });
    }

    private CompletableFuture<String> fallbackPlaceOrder(OrderRequest orderRequest, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please try again later!");
    }

    private static Order mapToOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkuCode(orderRequest.skuCode());
        return order;
    }



    public List<OrderResponse> getAllOrder() {
        List<Order> orders = orderRepository.findAll();

        return orders
                .stream()
                .map(this::getOrderResponse)
                .toList();
    }

    private OrderResponse getOrderResponse(Order order){

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .skuCode(order.getSkuCode())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .build();
    }
}
