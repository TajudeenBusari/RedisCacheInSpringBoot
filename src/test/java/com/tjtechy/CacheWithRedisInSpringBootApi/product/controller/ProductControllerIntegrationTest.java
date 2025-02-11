package com.tjtechy.CacheWithRedisInSpringBootApi.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.dto.CreateProductRequestDto;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.entity.dto.UpdateProductRequestDto;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.system.StatusCode;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.embedded.RedisServer;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers


public class ProductControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String BASE_URL = "/api/v1/product";

  private static RedisServer redisServer;

  @Container
  public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.0")
          .withDatabaseName("testdb")
          .withUsername("testuser")
          .withPassword("testpassword")
          .waitingFor(Wait.forListeningPort()); // Ensures it is ready before Spring connects

  @BeforeAll
  static void startContainers() {
    postgreSQLContainer.start(); // ✅ Start PostgreSQL Testcontainer
    System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
    System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
    System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    redisServer = new RedisServer();// Let it bind to a random port
    redisServer.start();
    System.setProperty("spring.redis.port", String.valueOf(redisServer.ports().get(0))); // Override Redis port
  }

  @AfterAll
  static void stopContainers() {
    redisServer.stop();
    postgreSQLContainer.stop();
  }

  @Test
  @DisplayName("Check Add Product (POST /api/v1/product) and Get Product By Id (GET /api/v1/product/{productId})")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void createProduct() throws Exception {
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
    var postResult = mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Add Success"))
            .andExpect(jsonPath("$.data.productId").isNotEmpty())
            .andExpect(jsonPath("$.data.productName").value("test product1"))
            .andExpect(jsonPath("$.data.productCategory").value("test product1 category"))
            .andExpect(jsonPath("$.data.productDescription").value("test product1 description"))
            .andExpect(jsonPath("$.data.productPrice").value(1000))
            .andExpect(jsonPath("$.data.productionDate").doesNotExist())
            .andExpect(jsonPath("$.data.expiryDate").isNotEmpty())
            .andReturn();
    var responseContent = postResult.getResponse().getContentAsString();
    System.out.println(responseContent);
    var responseDto = new JSONObject(responseContent);
    var productId = responseDto.getJSONObject("data").getString("productId");

    //find by id
    mockMvc.perform(get(BASE_URL + "/" + productId))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Get One Success"))
            .andExpect(jsonPath("$.data.productId").value(productId))
            .andExpect(jsonPath("$.data.productName").value("test product1"))
            .andExpect(jsonPath("$.data.productCategory").value("test product1 category"))
            .andExpect(jsonPath("$.data.productDescription").value("test product1 description"))
            .andExpect(jsonPath("$.data.productPrice").value(1000))
            .andExpect(jsonPath("$.data.productionDate").doesNotExist())
            .andExpect(jsonPath("$.data.expiryDate").isNotEmpty());
  }

  @Test
  @DisplayName("Check Create (POST /api/v1/product) and Get All Products (GET /api/v1/product)")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void getAllProducts() throws Exception {
    //given
    var createProductRequestDto = new CreateProductRequestDto(
            "test product2",
            "test product2 category",
            "test product2 description",
            1000,
            LocalDate.now().plusDays(90),
            LocalDateTime.now()
    );

    var json = objectMapper.writeValueAsString(createProductRequestDto);
    mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Add Success"));

    var response = mockMvc.perform(get(BASE_URL))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Get All Success"))
            .andExpect(jsonPath("$.data").isNotEmpty()).andReturn();
    var responseContent = response.getResponse().getContentAsString();
    System.out.println(responseContent);
  }

  @Test
  @DisplayName("Check Create (POST /api/v1/product) and Update Product (PUT /api/v1/product/{productId})")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void updateProduct() throws Exception{
    //given
    var createProductRequestDto = new CreateProductRequestDto(
            "test product3",
            "test product3 category",
            "test product3 description",
            1000,
            LocalDate.now().plusDays(90),
            LocalDateTime.now()
    );

    var json = objectMapper.writeValueAsString(createProductRequestDto);
    var postResult = mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Add Success"))
            .andExpect(jsonPath("$.data.productId").isNotEmpty())
            .andExpect(jsonPath("$.data.productName").value("test product3"))
            .andExpect(jsonPath("$.data.productCategory").value("test product3 category"))
            .andExpect(jsonPath("$.data.productDescription").value("test product3 description"))
            .andExpect(jsonPath("$.data.productPrice").value(1000))
            .andExpect(jsonPath("$.data.productionDate").doesNotExist())
            .andExpect(jsonPath("$.data.expiryDate").isNotEmpty())
            .andReturn();
    var responseContent = postResult.getResponse().getContentAsString();
    System.out.println(responseContent);
    var responseDto = new JSONObject(responseContent);
    var productId = responseDto.getJSONObject("data").getString("productId");

    //update
    var updateProductRequestDto = new UpdateProductRequestDto(
            "test product3 updated",
            "test product3 category updated",
            "test product3 description updated",
            2000

    );

    var updateJson = objectMapper.writeValueAsString(updateProductRequestDto);
    mockMvc.perform(put(BASE_URL + "/" + productId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Update Success"))
            .andExpect(jsonPath("$.data.productId").value(productId))
            .andExpect(jsonPath("$.data.productName").value("test product3 updated"))
            .andExpect(jsonPath("$.data.productCategory").value("test product3 category updated"))
            .andExpect(jsonPath("$.data.productDescription").value("test product3 description updated"))
            .andExpect(jsonPath("$.data.productPrice").value(2000))
            .andExpect(jsonPath("$.data.productionDate").doesNotExist())
            .andExpect(jsonPath("$.data.expiryDate").isNotEmpty());
  }

  @Test
  @DisplayName("Check Create (POST /api/v1/product) and Delete Product (DELETE /api/v1/product/{productId})")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void deleteProduct() throws Exception{
    //given
    var createProductRequestDto = new CreateProductRequestDto(
            "test product4",
            "test product4 category",
            "test product4 description",
            1000,
            LocalDate.now().plusDays(90),
            LocalDateTime.now()
    );

    var json = objectMapper.writeValueAsString(createProductRequestDto);
    var postResult = mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Add Success"))
            .andExpect(jsonPath("$.data.productId").isNotEmpty())
            .andExpect(jsonPath("$.data.productName").value("test product4"))
            .andExpect(jsonPath("$.data.productCategory").value("test product4 category"))
            .andExpect(jsonPath("$.data.productDescription").value("test product4 description"))
            .andExpect(jsonPath("$.data.productPrice").value(1000))
            .andExpect(jsonPath("$.data.productionDate").doesNotExist())
            .andExpect(jsonPath("$.data.expiryDate").isNotEmpty())
            .andReturn();
    var responseContent = postResult.getResponse().getContentAsString();
    System.out.println(responseContent);
    var responseDto = new JSONObject(responseContent);
    var productId = responseDto.getJSONObject("data").getString("productId");

    //delete
    mockMvc.perform(delete(BASE_URL + "/" + productId))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Delete Success"));
  }
}
