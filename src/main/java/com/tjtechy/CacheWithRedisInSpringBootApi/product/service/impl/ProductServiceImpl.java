package com.tjtechy.CacheWithRedisInSpringBootApi.product.service.impl;

import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.Product;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.exception.ProductNotFoundException;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.repository.ProductRepository;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.service.ProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Cacheable(value = "products") //store in "products" cache
    @Override
    public List<Product> getAllProducts() {
        System.out.println("*******Fetching products from database*******");
        var products = productRepository.findAll();
        return products;
    }

    @Cacheable(value = "product", key = "#productId") //store in "products" cache with key as id
    @Override
    public Product getProductById(UUID productId) {
        var foundProduct = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return foundProduct;
    }

    @CachePut(value = "product", key = "#product.productId") //store in "products" cache with key as id
    @Override
    public Product saveProduct(Product product) {

        return productRepository.save(product);
    }

    /***
     * Update product
     * Remove the production date and expiry date from the updateProduct method,
     * as they are not part of the update request dto.
     * User should not be able to update the production date and expiry date of a product.
     */
    @CachePut(value = "product", key = "#productId") //update cache with new value
    @Override
    public Product updateProduct(UUID productId, Product product) {
        var foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        foundProduct.setProductName(product.getProductName());
        foundProduct.setProductCategory(product.getProductCategory());
        foundProduct.setProductDescription(product.getProductDescription());
        foundProduct.setProductPrice(product.getProductPrice());

        //foundProduct.setProductionDate(product.getProductionDate());
        //foundProduct.setExpiryDate(product.getExpiryDate());
        return productRepository.save(foundProduct);

    }

    @CacheEvict(value = "product", key = "#productId") //delete cache with key as id
    @Override
    public void deleteProduct(UUID productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        productRepository.deleteById(productId);

    }

    @CacheEvict(value = "products", allEntries = true) //delete all cache
    @Override
    public void clearAllCache() {
        System.out.println("*******Clearing all cache*******");
    }
}
/**From java 21 above, the JDK restricts the ability of libraries to attach a Java agent
 * to their own JVM.As a result, the inline-mock-maker might not be able to function without an
 * explicit setup to enable instrumentation, and the JVM will always display a warning.
 * MORE INFO:
 * https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#0.3
 * No issue with jdk17 and below
 */
