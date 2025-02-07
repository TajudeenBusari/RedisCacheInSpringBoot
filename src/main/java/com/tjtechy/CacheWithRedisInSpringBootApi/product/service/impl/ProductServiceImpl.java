package com.tjtechy.CacheWithRedisInSpringBootApi.product.service.impl;

import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.Product;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.repository.ProductRepository;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return null;
    }

    @Override
    public Product getProductById(UUID productId) {
        return null;}

    @Override
    public Product saveProduct(Product product) {
      return null;
    }

    @Override
    public Product updateProduct(UUID productId, Product product) {
      return null;
    }

    @Override
    public void deleteProduct(UUID productId) {

    }
}
