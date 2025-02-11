package com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateProductRequestDto(
        @NotEmpty(message = "Product name is required")
    String productName,

    @NotEmpty(message = "Product category is required")
    String productCategory,

    @NotEmpty(message = "Product description is required")
    String productDescription,

        @NotNull(message = "Product price is required")
    double productPrice

) {

}
