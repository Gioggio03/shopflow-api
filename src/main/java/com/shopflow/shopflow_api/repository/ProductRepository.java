package com.shopflow.shopflow_api.repository;

import com.shopflow.shopflow_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

//Interfaccia perchè sarà spring a creare il bean

public interface ProductRepository extends JpaRepository<Product, Long> {
}