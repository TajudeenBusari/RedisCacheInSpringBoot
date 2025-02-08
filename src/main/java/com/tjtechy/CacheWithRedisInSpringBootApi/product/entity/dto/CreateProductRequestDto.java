package com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateProductRequestDto (
    @NotEmpty(message = "Product name is required")
    String productName,

    @NotEmpty(message = "Product category is required")
    String productCategory,

    @NotEmpty(message = "Product description is required")
    String productDescription,

    @Positive
            @NotNull(message = "Product price is required")
    double productPrice,

    @NotNull(message = "Expiry date is required")
    @FutureOrPresent(message = "Expiry date cannot be in the past")
    LocalDate expiryDate,

    @NotNull(message = "Production date is required")
    @PastOrPresent(message = "Production date cannot be in the future")
    LocalDateTime productionDate

){



}
