package com.tjtechy.CacheWithRedisInSpringBootApi.product.repository;

import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    //this is an interface that extends the JpaRepository
    // has two params, the type of entity and the primary key
    //this interface is used to perform CRUD operations on the entity
    //the JpaRepository has all the methods to perform CRUD operations
    //we can also define our own methods here
    //the JpaRepository is a generic interface
}
