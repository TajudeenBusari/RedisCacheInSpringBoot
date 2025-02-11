package com.tjtechy.CacheWithRedisInSpringBootApi.product.controller;

import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.dto.CreateProductRequestDto;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.dto.UpdateProductRequestDto;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.mapper.ProductMapper;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.service.ProductService;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.system.Result;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("${api.endpoint.base-url}/product")
public class ProductController {

  private final ProductService productService; //inject the interface

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  /**
   * This method is used to create a new product.
   * Remember to add the @Valid annotation to the request dto to enable validation
   */
  public Result createProduct(@Valid @RequestBody CreateProductRequestDto createProductRequestDto){
    //convert the request dto to entity
    var product = ProductMapper.mapFromCreateProductDtoToProduct(createProductRequestDto);
    var newProduct = productService.saveProduct(product);
    //convert the entity to response dto
    var productDto = ProductMapper.mapFromProductToProductDto(newProduct);
    return new Result("Add Success", true, productDto, StatusCode.SUCCESS);
  }

  @GetMapping
  /**
   * This method is used to get all products
   * and return a list of product dto
   */
  public Result getAllProducts(){
    var products = productService.getAllProducts();
    var productDtos = ProductMapper.mapFromProductListToProductDtoList(products);
    return new Result("Get All Success", true, productDtos, StatusCode.SUCCESS);
  }

  @GetMapping("/{productId}")
  /**
   * This method is used to get a product by id
   * and return a product dto
   */
  public Result getProductById(@PathVariable UUID productId){
    var product = productService.getProductById(productId);
    var productDto = ProductMapper.mapFromProductToProductDto(product);
    return new Result("Get One Success", true, productDto, StatusCode.SUCCESS);
  }

  @PutMapping("/{productId}")
  /**
   * This method is used to update a product by id
   * and return a product dto
   */
  public Result updateProductById(@PathVariable UUID productId, @Valid @RequestBody UpdateProductRequestDto updateProductRequestDto){
    var product = ProductMapper.mapFromUpdateProductDtoToProduct(updateProductRequestDto);
    var updatedProduct = productService.updateProduct(productId, product);
    var productDto = ProductMapper.mapFromProductToProductDto(updatedProduct);
    return new Result("Update Success", true, productDto, StatusCode.SUCCESS);
  }
  @DeleteMapping("/{productId}")
  /**
   * This method is used to delete a product by id
   * and return a success message
   * if the product is deleted successfully
   */
  public Result deleteProductById(@PathVariable UUID productId){
    productService.deleteProduct(productId);
    return new Result("Delete Success", true, null, StatusCode.SUCCESS);
  }

  @DeleteMapping("/clear-cache")
  /**
   * This method is used to clear all cache
   * and return a success message
   * if the cache is cleared successfully
   */
  public Result clearAllCache(){
    productService.clearAllCache();
    return new Result("Clear Cache Success", true, null, StatusCode.SUCCESS);
  }


}
