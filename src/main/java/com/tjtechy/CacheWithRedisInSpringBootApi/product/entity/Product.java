package com.tjtechy.CacheWithRedisInSpringBootApi.product.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID productId;
  private String productName;
  private String productCategory;
  private String productDescription;
  private double productPrice;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime productionDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate expiryDate;


  public Product() {
  }
  public Product(UUID productId, String productName, String productCategory, String productDescription, double productPrice, LocalDateTime productionDate, LocalDate expiryDate) {
    this.productId = productId;
    this.productName = productName;
    this.productCategory = productCategory;
    this.productDescription = productDescription;
    this.productPrice = productPrice;
    this.productionDate = productionDate;
    this.expiryDate = expiryDate;
  }
  public UUID getProductId() {
    return productId;
  }
  public void setProductId(UUID productId) {
    this.productId = productId;
  }
  public String getProductName() {
    return productName;
  }
  public void setProductName(String productName) {
    this.productName = productName;
  }
  public String getProductCategory() {
    return productCategory;
  }
  public void setProductCategory(String productCategory) {
    this.productCategory = productCategory;
  }
  public String getProductDescription() {
    return productDescription;
  }
  public void setProductDescription(String productDescription) {
    this.productDescription = productDescription;
  }
  public double getProductPrice() {
    return productPrice;
  }
  public void setProductPrice(double productPrice) {
    this.productPrice = productPrice;
  }
  public LocalDateTime getProductionDate() {
    return productionDate;
  }
  public void setProductionDate(LocalDateTime productionDate) {
    this.productionDate = productionDate;
  }
  public LocalDate getExpiryDate() {
    return expiryDate;
  }
  public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
  }

}
/**
 * Guid is not a recognise data type in java, the preferred data
 * type is UUID. However, Java provides the UUID (Universally Unique Identifier) class
 *  in the java.util package, which serves the same purpose.
 *
 *  //To create a table in the database:
 *  Create the entity class
 *  The entity class is a POJO class that is used to create objects for the database table.
 *  Create a repository interface
 *  Configure the application.properties or application.yml file
 *  Create database schema
 *  Run the application
 *  This is enough to create the application database and table.
 */