package com.santojeet.sbproject.repository;

import com.santojeet.sbproject.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
