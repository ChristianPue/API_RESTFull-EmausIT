package com.emausit.api.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.emausit.api.enums.UserCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User 
{
  // Parámetros
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String firstname;
  private String lastname;
  private String username;
  private String email;
  private String password;
  private UserCategory category;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
 
  // Foreign Keys
  @OneToMany(mappedBy = "user")
  private List<Product> purchasedProducts;

  // Constructors
  public User() {}

  // Getters y Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String name) {
    this.firstname = name;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserCategory getCategory() {
    return category;
  }

  public void setCategory(UserCategory category) {
    this.category = category;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public List<Product> getPurchasedProducts() {
    return purchasedProducts;
  }

  public void setPurchasedProducts(List<Product> purchasedProducts) {
    this.purchasedProducts = purchasedProducts;
  }

  public void addProduct(Product product) {
    this.purchasedProducts.add(product);
  }

  public boolean deleteProduct(Integer id) {
    // Validar el id si se pasa del rango, es negativo
    if (id == null || purchasedProducts.isEmpty())
    {
      return false;
    } 

    // Recorrer la lista purchasedProducts y encontrar el producto que coincida con el id
    for (int i = 0; i < purchasedProducts.size(); i++)
    {
      if (purchasedProducts.get(i).getId().equals(id))
      {
        // Si se encontró eliminar el producto y devolver true
        purchasedProducts.remove(i);
        return true;
      }
    }
    
    // Si no se encontró devolver false
    return false;
  }
}
