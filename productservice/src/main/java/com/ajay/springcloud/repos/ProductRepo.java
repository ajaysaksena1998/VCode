package com.ajay.springcloud.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajay.springcloud.model.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {

}
