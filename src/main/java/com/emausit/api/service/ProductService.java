package com.emausit.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.emausit.api.entity.Product;
import com.emausit.api.entity.User;
import com.emausit.api.enums.ProductCategory;
import com.emausit.api.exception.InvalidInputException;
import com.emausit.api.exception.ResourceNotFoundException;
import com.emausit.api.repository.ProductRepository;

@Service
public class ProductService 
{
  @Autowired
  ProductRepository productRepository;

  @Autowired
  @Lazy // Para evitar dependencia circular
  private UserService userService;

  // --- OTROS ---
  public void isTextValid(String text)
  {
    if (text == null || text.trim().isEmpty())
    {
      throw new InvalidInputException("El texto no puede estar vacío.");
    }
  }
  public void isPriceValid(Double price)
  {
    if (price == null || price < 0.0) 
    {
      throw new InvalidInputException("El precio no puede ser nulo ni negativo.");
    }
  }
  public void isQuantityValid(Integer quantity)
  {
    if (quantity == null || quantity <= 0)
    {
      throw new InvalidInputException("La cantidad no puede ser nula o ser menor que 1.");
    }
  }
  public void isCategoryValid(ProductCategory category)
  {
    if (category == null)
    {
      throw new InvalidInputException("La categoría no puede ser nula.");
    }
  }

  // --- SERVICIOS ---
  // Obtener todos los productos existentes
  public List<Product> getProducts()
  {
    return productRepository.findAll();
  }

  // Crear un producto
  public Product createProduct(Integer userId, Product product)
  {
    // Guardado del usuario
    User user = userService.getUserById(userId);
    product.setUser(user);

    // Validaciones
    isTextValid(product.getName());
    isTextValid(product.getDescription());
    isPriceValid(product.getPrice());
    isQuantityValid(product.getQuantity());
    isCategoryValid(product.getCategory());

    // Obtener el tiempo actual
    product.setCreatedAt(LocalDateTime.now());
    product.setUpdatedAt(product.getCreatedAt());

    return productRepository.save(product);
  }

  // Obtener producto según su id
  public Product getProductById(Integer id)
  {
    return productRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + id + " no encontrado."));
  }

  // Actualizar un producto según su id
  public Product updateProductById(Integer id, Product updatedProduct)
  {
    // Obtener el producto por su id
    Product originProduct = getProductById(id);

    // Validaciones
    isTextValid(updatedProduct.getName());
    isTextValid(updatedProduct.getDescription());
    isPriceValid(updatedProduct.getPrice());
    isQuantityValid(updatedProduct.getQuantity());
    isCategoryValid(updatedProduct.getCategory());

    // Actualizaciones
    originProduct.setName(updatedProduct.getName());
    originProduct.setDescription(updatedProduct.getDescription());
    originProduct.setPrice(updatedProduct.getPrice());
    originProduct.setQuantity(updatedProduct.getQuantity());
    originProduct.setCategory(updatedProduct.getCategory());

    // Fecha de actualizacion
    originProduct.setUpdatedAt(LocalDateTime.now());

    return productRepository.save(originProduct);
  }

  // Eliminar un producto según su id
  public String deleteProductById(Integer id)
  {
    if (!productRepository.existsById(id))
    {
      throw new ResourceNotFoundException("Producto con ID " + id + " no encontrado.");
    }

    productRepository.deleteById(id);
    return "Producto con ID " + id + " eliminado correctamente.";
  }

  // Obtener todos los productos en venta de un usuario según su id


  // Obtener todos los productos comprados de un usuario


}
