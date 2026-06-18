package com.shopflow.shopflow_api.service;

import com.shopflow.shopflow_api.dto.OrderItemRequest;
import com.shopflow.shopflow_api.model.Order;
import com.shopflow.shopflow_api.model.OrderItem;
import com.shopflow.shopflow_api.model.OrderStatus;
import com.shopflow.shopflow_api.model.Product;
import com.shopflow.shopflow_api.model.User;
import com.shopflow.shopflow_api.repository.OrderRepository;
import com.shopflow.shopflow_api.repository.ProductRepository;
import com.shopflow.shopflow_api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order placeOrder(String userEmail, List<OrderItemRequest> items) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : items) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Product not found: " + itemReq.productId()));

            if (product.getStock() < itemReq.quantity()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Stock insufficiente per: " + product.getName());
            }

            // scala lo stock (modifica automaticamente persistita: dirty checking)
            product.setStock(product.getStock() - itemReq.quantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemReq.quantity());
            orderItem.setPrice(product.getPrice()); // scatto del prezzo
            order.getItems().add(orderItem);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())));
        }

        order.setTotal(total);
        return orderRepository.save(order);
    }

    public List<Order> findByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        return orderRepository.findByUserId(user.getId());
    }
}