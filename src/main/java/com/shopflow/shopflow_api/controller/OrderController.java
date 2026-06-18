package com.shopflow.shopflow_api.controller;

import com.shopflow.shopflow_api.dto.OrderItemResponse;
import com.shopflow.shopflow_api.dto.OrderResponse;
import com.shopflow.shopflow_api.dto.PlaceOrderRequest;
import com.shopflow.shopflow_api.model.Order;
import com.shopflow.shopflow_api.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request,
                                                    Authentication authentication) {
        Order order = orderService.placeOrder(authentication.getName(), request.items());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(location).body(toResponse(order));
    }

    @GetMapping
    public List<OrderResponse> myOrders(Authentication authentication) {
        return orderService.findByUser(authentication.getName()).stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderItemResponse(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPrice()))
                .toList();
        return new OrderResponse(order.getId(), order.getTotal(),
                order.getStatus(), order.getCreatedAt(), items);
    }
}