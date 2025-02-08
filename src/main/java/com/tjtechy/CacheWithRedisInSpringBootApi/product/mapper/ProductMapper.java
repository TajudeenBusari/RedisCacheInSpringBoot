package com.tjtechy.CacheWithRedisInSpringBootApi.product.mapper;

import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.Product;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.dto.CreateProductRequestDto;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.dto.ProductDto;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.dto.UpdateProductRequestDto;

import java.util.List;

public class ProductMapper {
  public static ProductDto mapFromProductToProductDto(Product product) {
    return new ProductDto(
            product.getProductId(),
            product.getProductName(),
            product.getProductCategory(),
            product.getProductDescription(),
            product.getProductPrice(),
            product.getExpiryDate()
           );
  }
  public static Product mapFromProductDtoToProduct(ProductDto productDto) {
    return new Product(
            productDto.productId(),
            productDto.productName(),
            productDto.productCategory(),
            productDto.productDescription(),
            productDto.productPrice(),
            null,
            productDto.expiryDate()
           );
  }

  public static List<ProductDto> mapFromProductListToProductDtoList(List<Product> productList) {
    return productList
            .stream()
            .map(ProductMapper::mapFromProductToProductDto)
            .toList();
  }

  public static Product mapFromCreateProductDtoToProduct(CreateProductRequestDto createProductRequestDto) {
    return new Product(

            null,
            createProductRequestDto.productName(),
            createProductRequestDto.productCategory(),
            createProductRequestDto.productDescription(),
            createProductRequestDto.productPrice(),
            createProductRequestDto.productionDate(),
            createProductRequestDto.expiryDate()
           );
  }

  public static Product mapFromUpdateProductDtoToProduct(UpdateProductRequestDto updateProductRequestDto) {
    return new Product(
            null,
            updateProductRequestDto.productName(),
            updateProductRequestDto.productCategory(),
            updateProductRequestDto.productDescription(),
            updateProductRequestDto.productPrice(),
            null,
            null
           );
  }

}
