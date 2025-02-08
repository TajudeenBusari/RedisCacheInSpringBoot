package com.tjtechy.CacheWithRedisInSpringBootApi.product.service.impl;

import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.Product;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.exception.ProductNotFoundException;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductServiceImpl productService;

  List<Product> productList;

  @BeforeEach
  void setUp() {
    productList = new ArrayList<>();

    var product1 = new Product();
    product1.setProductId(UUID.randomUUID());
    product1.setProductName("Product 1");
    product1.setProductCategory("Category 1");
    product1.setProductDescription("Description 1");
    product1.setProductPrice(100.0);
    product1.setProductionDate(LocalDateTime.now());
    product1.setExpiryDate(LocalDate.now().plusDays(30));
    productList.add(product1);

    var product2 = new Product();
    product2.setProductId(UUID.randomUUID());
    product2.setProductName("Product 2");
    product2.setProductCategory("Category 2");
    product2.setProductDescription("Description 2");
    product2.setProductPrice(200.0);
    product2.setProductionDate(LocalDateTime.now());
    product2.setExpiryDate(LocalDate.now().plusDays(60));

  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getAllProducts() {
    // given
    given(productRepository.findAll()).willReturn(productList);

    // when
    var actualProducts = productService.getAllProducts();

    // then
    assertNotNull(actualProducts);
    assertThat(actualProducts.size()).isEqualTo(productList.size());
  }

  @Test
  void getProductByIdSuccess() {
    //given
    given(productRepository.findById(productList.get(0).getProductId())).willReturn(Optional.of(productList.get(0)));
    //when
    var actualProduct = productService.getProductById(productList.get(0).getProductId());

    //then
    assertNotNull(actualProduct);
    assertThat(actualProduct.getProductId()).isEqualTo(productList.get(0).getProductId());
    assertThat(actualProduct.getProductName()).isEqualTo(productList.get(0).getProductName());
    assertThat(actualProduct.getProductCategory()).isEqualTo(productList.get(0).getProductCategory());
    assertThat(actualProduct.getProductDescription()).isEqualTo(productList.get(0).getProductDescription());
    assertThat(actualProduct.getProductPrice()).isEqualTo(productList.get(0).getProductPrice());
    assertThat(actualProduct.getProductionDate()).isEqualTo(productList.get(0).getProductionDate());
    assertThat(actualProduct.getExpiryDate()).isEqualTo(productList.get(0).getExpiryDate());
  }

  @Test
  void getProductByIdFailure() {
    //given
    given(productRepository.findById(productList.get(0).getProductId())).willReturn(Optional.empty());
    //when and then
    assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productList.get(0).getProductId()));
  }

  @Test
  void saveProductSuccess() {
    //given
    var product = new Product();
    product.setProductId(UUID.randomUUID());
    product.setProductName("Product 3");
    product.setProductCategory("Category 3");
    product.setProductDescription("Description 3");
    product.setProductPrice(300.0);
    product.setProductionDate(LocalDateTime.now());
    product.setExpiryDate(LocalDate.now().plusDays(90));

    given(productRepository.save(product)).willReturn(product);

    //when
    var savedProduct = productService.saveProduct(product);

    //then
    assertNotNull(savedProduct);
    assertThat(savedProduct.getProductId()).isEqualTo(product.getProductId());
    assertThat(savedProduct.getProductName()).isEqualTo(product.getProductName());
    assertThat(savedProduct.getProductCategory()).isEqualTo(product.getProductCategory());
    assertThat(savedProduct.getProductDescription()).isEqualTo(product.getProductDescription());
    assertThat(savedProduct.getProductPrice()).isEqualTo(product.getProductPrice());
    assertThat(savedProduct.getProductionDate()).isEqualTo(product.getProductionDate());
    assertThat(savedProduct.getExpiryDate()).isEqualTo(product.getExpiryDate());

  }

  @Test
  void updateProductSuccess() {
    //given
    var product = new Product();
    product.setProductId(UUID.randomUUID());
    product.setProductName("some Product 3");
    product.setProductCategory("Some Category 3");
    product.setProductDescription("Some Description 3");
    product.setProductPrice(300.0);
    product.setProductionDate(LocalDateTime.now());
    product.setExpiryDate(LocalDate.now().plusDays(120));

    var updatedProduct = new Product();
    updatedProduct.setProductId(product.getProductId());
    updatedProduct.setProductName("Updated Product 3");
    updatedProduct.setProductCategory("Updated Category 3");
    updatedProduct.setProductDescription("Updated Description 3");
    updatedProduct.setProductPrice(300.0);
    updatedProduct.setProductionDate(LocalDateTime.now());
    updatedProduct.setExpiryDate(LocalDate.now().plusDays(120));

    given(productRepository.findById(product.getProductId())).willReturn(Optional.of(product));
    given(productRepository.save(product)).willReturn(product);

    //when
    var updated = productService.updateProduct(product.getProductId(), updatedProduct);

    //then
    assertNotNull(updated);
    assertThat(updated.getProductId()).isEqualTo(product.getProductId());
    assertThat(updated.getProductName()).isEqualTo(updatedProduct.getProductName());
    assertThat(updated.getProductCategory()).isEqualTo(updatedProduct.getProductCategory());
    assertThat(updated.getProductDescription()).isEqualTo(updatedProduct.getProductDescription());
    assertThat(updated.getProductPrice()).isEqualTo(updatedProduct.getProductPrice());
    assertThat(updated.getProductionDate()).isEqualTo(updatedProduct.getProductionDate());
    assertThat(updated.getExpiryDate()).isEqualTo(updatedProduct.getExpiryDate());
    verify(productRepository, times(1)).findById(product.getProductId());
    verify(productRepository, times(1)).save(product);
  }

  @Test
  void updateProductFailure() {
    //given
    var product = new Product();
    product.setProductId(UUID.randomUUID());
    product.setProductName("some Product 3");
    product.setProductCategory("Some Category 3");
    product.setProductDescription("Some Description 3");
    product.setProductPrice(300.0);
    product.setProductionDate(LocalDateTime.now());
    product.setExpiryDate(LocalDate.now().plusDays(120));

    var updatedProduct = new Product();
    updatedProduct.setProductId(product.getProductId());
    updatedProduct.setProductName("Updated Product 3");
    updatedProduct.setProductCategory("Updated Category 3");
    updatedProduct.setProductDescription("Updated Description 3");
    updatedProduct.setProductPrice(300.0);
    updatedProduct.setProductionDate(LocalDateTime.now());
    updatedProduct.setExpiryDate(LocalDate.now().plusDays(120));

    given(productRepository.findById(product.getProductId())).willReturn(Optional.empty());

    //when and then
    assertThrows(ProductNotFoundException.class, () ->
            productService.updateProduct(product.getProductId(), updatedProduct));
  }

  @Test
  void deleteProduct() {
    //given
    var product = new Product();
    product.setProductId(UUID.randomUUID());
    product.setProductName("Product 3");
    product.setProductCategory("Category 3");
    product.setProductDescription("Description 3");
    product.setProductPrice(300.0);
    product.setProductionDate(LocalDateTime.now());
    product.setExpiryDate(LocalDate.now().plusDays(90));

    given(productRepository.findById(product.getProductId())).willReturn(Optional.of(product));

    //when
    productService.deleteProduct(product.getProductId());

    //then
    verify(productRepository, times(1)).deleteById(product.getProductId());

  }
}
/**FOR TESTS:
 * From java 21 above, the JDK restricts the ability of libraries to attach a Java agent
 * to their own JVM.As a result, the inline-mock-maker might not be able to function without an
 * explicit setup to enable instrumentation, and the JVM will always display a warning.
 * MORE INFO:
 * https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#0.3
 * No issue with jdk17 and below
 */