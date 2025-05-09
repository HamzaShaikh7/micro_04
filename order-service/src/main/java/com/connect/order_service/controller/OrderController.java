package com.connect.order_service.controller;

import com.connect.order_service.dto.OrderRequest;
import com.connect.order_service.dto.OrderResponse;
import com.connect.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
public class OrderController {


    @Autowired
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrder(){
        return orderService.getAllOrder();
    }
}
