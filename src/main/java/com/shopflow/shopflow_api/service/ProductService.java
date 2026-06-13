package com.shopflow.shopflow_api.service;

import com.shopflow.shopflow_api.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    // dati di esempio, così abbiamo qualcosa da leggere
    public ProductService() {
        products.add(new Product(idCounter.incrementAndGet(),
                "Cuffie Bluetooth", "Cuffie wireless over-ear",
                new BigDecimal("49.90"), 30));
        products.add(new Product(idCounter.incrementAndGet(),
                "Mouse ergonomico", "Mouse verticale wireless",
                new BigDecimal("29.90"), 50));
    }

    public List<Product> findAll() {
        return products;
    }

    public Optional<Product> findById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Product create(Product product) {
        product.setId(idCounter.incrementAndGet());
        products.add(product);
        return product;
    }

    public Optional<Product> update(Long id, Product updated) {
        return findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setPrice(updated.getPrice());
            existing.setStock(updated.getStock());
            return existing;
        });
    }

    public boolean delete(Long id) {
        return products.removeIf(p -> p.getId().equals(id));
    }
}