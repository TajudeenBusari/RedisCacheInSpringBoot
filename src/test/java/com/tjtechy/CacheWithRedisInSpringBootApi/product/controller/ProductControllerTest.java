package com.tjtechy.CacheWithRedisInSpringBootApi.product.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.Product;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.dto.CreateProductRequestDto;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.dto.UpdateProductRequestDto;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.exception.ProductNotFoundException;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * The ProductControllerTest class is responsible for testing the ProductController class.
 * use MockitoBean instead of mock so that test results will not be stored in the database.
 */
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
class ProductControllerTest {

  @MockitoBean
  private ProductService productService;



  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MockMvc mockMvc;

  @Value("${api.endpoint.base-url}")
  private String baseUrl;

  List<Product> productList;

  @BeforeEach
  void setUp() {
    productList = new ArrayList<>();
    var product1 = new Product();
    product1.setProductId(UUID.randomUUID());
    product1.setProductName("product1");
    product1.setProductPrice(1000);
    product1.setProductDescription("product1 description");
    product1.setProductCategory("product1 category");
    product1.setProductionDate(LocalDateTime.now());
    product1.setExpiryDate(LocalDate.now().plusDays(30));
    productList.add(product1);

    var product2 = new Product();
    product2.setProductId(UUID.randomUUID());
    product2.setProductName("product2");
    product2.setProductPrice(2000);
    product2.setProductDescription("product2 description");
    product2.setProductCategory("product2 category");
    product2.setProductionDate(LocalDateTime.now());
    product2.setExpiryDate(LocalDate.now().plusDays(60));
    productList.add(product2);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void createProductSuccess() throws Exception {
    //given
    var createProductRequestDto = new CreateProductRequestDto(
          "test product1",
          "test product1 category",
          "test product1 description",
          1000,
          LocalDate.now().plusDays(90),
          LocalDateTime.now()
    );

    var json = objectMapper.writeValueAsString(createProductRequestDto);

    //mock the saveProduct method of the productService
    var savedProduct = new Product();
    savedProduct.setProductId(UUID.randomUUID());
    savedProduct.setProductName("test product1");
    savedProduct.setProductPrice(1000);
    savedProduct.setProductDescription("test product1 description");
    savedProduct.setProductCategory("test product1 category");
    savedProduct.setProductionDate(LocalDateTime.now());
    savedProduct.setExpiryDate(LocalDate.now().plusDays(90));

    given(productService.saveProduct(any(Product.class))).willReturn(savedProduct);

    //when and //then
    mockMvc.perform(post(baseUrl + "/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Add Success"))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.data.productId").isNotEmpty())
            .andExpect(jsonPath("$.data.productName").value("test product1"))
            .andExpect(jsonPath("$.data.productCategory").value("test product1 category"))
            .andExpect(jsonPath("$.data.productDescription").value("test product1 description"))
            .andExpect(jsonPath("$.data.productPrice").value(1000))
            //production date is not returned in the response because it is not in the response productDto.
            //Don't use notEmpty() because it will check if the value is not null or empty but there is no value in the response.
            .andExpect(jsonPath("$.data.productionDate").doesNotExist())
            .andExpect(jsonPath("$.data.expiryDate").isNotEmpty());
  }


  @Test
  void getAllProductsSuccess() throws Exception {
    //given
    given(productService.getAllProducts()).willReturn(productList);

    //when and //then
    mockMvc.perform(get(baseUrl + "/product")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Get All Success"))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].productId").isNotEmpty())
            .andExpect(jsonPath("$.data[0].productName").value("product1"))
            .andExpect(jsonPath("$.data[0].productCategory").value("product1 category"))
            .andExpect(jsonPath("$.data[0].productDescription").value("product1 description"))
            .andExpect(jsonPath("$.data[0].productPrice").value(1000))
            .andExpect(jsonPath("$.data[0].productionDate").doesNotExist())
            .andExpect(jsonPath("$.data[0].expiryDate").isNotEmpty())
            .andExpect(jsonPath("$.data[1].productId").isNotEmpty())
            .andExpect(jsonPath("$.data[1].productName").value("product2"))
            .andExpect(jsonPath("$.data[1].productCategory").value("product2 category"))
            .andExpect(jsonPath("$.data[1].productDescription").value("product2 description"))
            .andExpect(jsonPath("$.data[1].productPrice").value(2000))
            .andExpect(jsonPath("$.data[1].productionDate").doesNotExist())
            .andExpect(jsonPath("$.data[1].expiryDate").isNotEmpty());
  }

  @Test
  void getProductByIdSuccess() throws Exception {
    //given
    given(productService.getProductById(productList.get(0).getProductId())).willReturn(productList.get(0));
    //when and //then
    mockMvc.perform(get(baseUrl + "/product/" + productList.get(0).getProductId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Get One Success"))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.data.productId").isNotEmpty())
            .andExpect(jsonPath("$.data.productName").value("product1"))
            .andExpect(jsonPath("$.data.productCategory").value("product1 category"))
            .andExpect(jsonPath("$.data.productDescription").value("product1 description"))
            .andExpect(jsonPath("$.data.productPrice").value(1000))
            .andExpect(jsonPath("$.data.productionDate").doesNotExist())
            .andExpect(jsonPath("$.data.expiryDate").isNotEmpty());
  }

  @Test
  void getProductByIdNotFound() throws Exception {
    //given
    given(productService.getProductById(UUID.fromString("89ec526e-4e5c-46e5-8ccc-e2299d7e7634")))
            .willThrow(new ProductNotFoundException(UUID.fromString("89ec526e-4e5c-46e5-8ccc-e2299d7e7634")));
    //when and //then
    mockMvc.perform(get(baseUrl + "/product/89ec526e-4e5c-46e5-8ccc-e2299d7e7634")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Product not found with id: 89ec526e-4e5c-46e5-8ccc-e2299d7e7634"))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void updateProductByIdSuccess() throws Exception {
    //given
    var updateProductRequestDto = new UpdateProductRequestDto(
            "test product1",
            "test product1 category",
            "test product1 description",
            1000

    );

    var json = objectMapper.writeValueAsString(updateProductRequestDto);

    var updatedProduct = new Product();

    updatedProduct.setProductId(UUID.fromString("89ec526e-4e5c-46e5-8ccc-e2299d7e7634"));
    updatedProduct.setProductName("test product1");
    updatedProduct.setProductPrice(1000);
    updatedProduct.setProductDescription("test product1 description");
    updatedProduct.setProductCategory("test product1 category");
    updatedProduct.setProductionDate(LocalDateTime.now());
    updatedProduct.setExpiryDate(LocalDate.now().plusDays(90));

    given(productService.updateProduct(eq(UUID.fromString("89ec526e-4e5c-46e5-8ccc-e2299d7e7634")), any(Product.class))).willReturn(updatedProduct);

    //when and then
    mockMvc.perform(put(baseUrl + "/product/89ec526e-4e5c-46e5-8ccc-e2299d7e7634")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Update Success"))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.data.productId").isNotEmpty())
            .andExpect(jsonPath("$.data.productName").value("test product1"))
            .andExpect(jsonPath("$.data.productCategory").value("test product1 category"))
            .andExpect(jsonPath("$.data.productDescription").value("test product1 description"))
            .andExpect(jsonPath("$.data.productPrice").value(1000))
            .andExpect(jsonPath("$.data.productionDate").doesNotExist())
            .andExpect(jsonPath("$.data.expiryDate").isNotEmpty());
  }

  @Test
  void deleteProductByIdSuccess() throws Exception {
    //given
    var productId = UUID.fromString("89ec526e-4e5c-46e5-8ccc-e2299d7e7634");
    doNothing().when(productService).deleteProduct(productId);
    //when and //then
    mockMvc.perform(delete(baseUrl + "/product/89ec526e-4e5c-46e5-8ccc-e2299d7e7634")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Delete Success"))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.data").doesNotExist());
  }
}