package com.connect.order_service.service;

import com.connect.order_service.config.ClientConfig;
import com.connect.order_service.dto.OrderRequest;
import com.connect.order_service.dto.OrderResponse;
import com.connect.order_service.model.Order;
import com.connect.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private ClientConfig clientConfig;

//    @Autowired
//    private RestTemplate restTemplate;


    public String placeOrder(OrderRequest orderRequest) {

        boolean isInStock = false;
//        String url = "http://localhost:8083/api/inventory?skuCode="+orderRequest.skuCode()+"&quantity="+orderRequest.quantity();
//        if(Boolean.TRUE.equals(restTemplate.getForObject(url, boolean.class))){

        if(clientConfig.checkInventory(orderRequest.skuCode(), orderRequest.quantity())){
            Order order = mapToOrder(orderRequest);
            orderRepository.save(order);
            return "Order placed.";
        }
        else {
            return "Product unavailable";

        }
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
