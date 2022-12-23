package com.ppzzl.learn.billling.repository;

import com.ppzzl.learn.billling.model.product.Product;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ProductRepository {
    private final Map<Long, Product> products = new HashMap<>();

    public void addProduct(Product product) {
        this.products.put(product.getProductId(), product);
    }

    public Optional<Product> getProduct(long productId) {
        return Optional.of(this.products.get(productId));
    }
}
