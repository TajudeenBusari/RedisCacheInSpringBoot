package com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ProductDto( UUID productId,
                         String productName,
                         String productCategory,
                         String productDescription,
                         double productPrice,
                         LocalDate expiryDate
                         ) {

}
