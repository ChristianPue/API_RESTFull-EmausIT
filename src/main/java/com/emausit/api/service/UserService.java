package com.emausit.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.emausit.api.entity.User;
import com.emausit.api.enums.UserCategory;
import com.emausit.api.exception.InvalidInputException;
import com.emausit.api.exception.ResourceNotFoundException;
import com.emausit.api.repository.UserRepository;

@Service
public class UserService 
{
  @Autowired
  UserRepository userRepository;

  @Autowired
  @Lazy // Para evitar dependencia circular
  private ProductService productService;

  // --- OTROS ---
  public void isNameValid(String name)
  {
    String NAME_REGEX = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$";
    Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    if (name == null || name.trim().isEmpty())
    {
      throw new InvalidInputException("El nombre no puede estar vacío.");
    }
    if (!NAME_PATTERN.matcher(name).matches()) 
    {
      throw new InvalidInputException("El nombre solo puede contener letras, espacios y tildes.");
    }
  }
  public void isUsernameValid(String username, boolean exists)
  {
    String USERNAME_REGEX = "^[A-Za-z0-9]+$";
    Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);

    if (username == null || username.trim().isEmpty())
    {
      throw new InvalidInputException("El nombre de usuario no puede estar vacío.");
    }

    if (!USERNAME_PATTERN.matcher(username).matches()) 
    {
      throw new InvalidInputException("El nombre de usuario solo puede contener letras y números.");
    }

    if (exists == false && userRepository.existsByUsername(username)) {
      throw new InvalidInputException("El nombre de usuario ya está en uso.");
    }
  }
  public void isEmailValid(String email, boolean exists)
  {
    String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,6}$";
    Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    if (email == null || email.trim().isEmpty())
    {
      throw new InvalidInputException("El correo electrónico no puede estar vacío.");
    }
    if (!EMAIL_PATTERN.matcher(email).matches())
    {
      throw new InvalidInputException("Formato del correo electrónico inválido");
    }
    if (exists == false && userRepository.existsByEmail(email))
    {
      throw new InvalidInputException("El correo electrónico ya está registrado.");
    }
  }
  public void isPasswordValid(String password, String oldPassword)
  {
    String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\p{Punct}]).{8,}$";
    Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    if (password == null || password.trim().isEmpty())
    {
      throw new InvalidInputException("La contraseña no puede estar vacía.");
    }
    if (!PASSWORD_PATTERN.matcher(password).matches())
    {
      throw new InvalidInputException("La contraseña debe tener al menos 8 caracteres, incluyendo una mayúscula, una minúscula, un número y un carácter especial.");
    }
    if (oldPassword != null && password.equals(oldPassword))
    {
      throw new InvalidInputException("La contraseña es igual a la contraseña antigua.");
    }
  }
  public void isUserCategoryValid(UserCategory category) // UserCategory is enum : ADMIN, USER, ORGANIZATION
  {
    if (category == null) 
    {
      throw new InvalidInputException("La categoría no puede ser nula.");
    }
  }

  // --- SERVICIOS ---
  // Obtener todos los usuarios
  public List<User> getUsers()
  {
    return userRepository.findAll();
  }

  // Crear un usuario
  public User createUser(User user) {
    // Validaciones:
    isNameValid(user.getFirstname());
    isNameValid(user.getLastname());
    isUsernameValid(user.getUsername(), false);
    isEmailValid(user.getEmail(), false);
    isPasswordValid(user.getPassword(), null);
    isUserCategoryValid(user.getCategory());

    // Obtener el tiempo actual
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(user.getCreatedAt());

    return userRepository.save(user);
  }

  // Obtener el usuario según su id
  public User getUserById(Integer id)
  {
    return userRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado."));
  }

  // Actualizar el usuario según su id
  public User updateUserById(Integer id, User updatedUser)
  {
    // Obtener el user por su id
    User originUser = getUserById(id);

    // Validaciones:
    isNameValid(updatedUser.getFirstname());
    isNameValid(updatedUser.getLastname());
    isUsernameValid(updatedUser.getUsername(), true);
    isEmailValid(updatedUser.getEmail(), true);
    isPasswordValid(updatedUser.getPassword(), originUser.getPassword());
    isUserCategoryValid(updatedUser.getCategory());

    // Actualización de los atributos:
    originUser.setFirstname(updatedUser.getFirstname());
    originUser.setLastname(updatedUser.getLastname());
    originUser.setUsername(updatedUser.getUsername());
    originUser.setEmail(updatedUser.getEmail());
    originUser.setPassword(updatedUser.getPassword());
    originUser.setCategory(updatedUser.getCategory());

    // Registra la fecha de actualización
    originUser.setUpdatedAt(LocalDateTime.now());

    return userRepository.save(originUser);
  }

  // Eliminar el usuario según su id
  public String deleteUserById(Integer id) 
  {
    if (!userRepository.existsById(id)) 
    {
        throw new ResourceNotFoundException("Usuario con ID " + id + " no encontrado.");
    }

    userRepository.deleteById(id);
    return "Usuario con ID " + id + " eliminado correctamente.";
  }
}
