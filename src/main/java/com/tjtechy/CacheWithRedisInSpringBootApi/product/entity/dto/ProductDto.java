package com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.dto;

public record ProductDto(String productName,
                         String productCategory,
                         String productDescription,
                         double productPrice) {

}
