package com.emausit.api.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler 
{
  // ResourceNotFoundException : No se encontró dicho registro
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handlerResourceNotFoundException(ResourceNotFoundException ex)
  {
    Map<String, Object> body = new HashMap<>();
    
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.NOT_FOUND.value());
    body.put("error", "Recurso no encontrado");
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  // BadRequestException : Se intentó hacer una petición prohibida o errónea
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> handlerBadRequestException(BadRequestException ex)
  {
    Map<String, Object> body = new HashMap<>();

    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Solicitud incorrecta");
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  // Otras excepciones
  // InvalidInputException : Se intentó guardar un valor de forma no válido
  @ExceptionHandler(InvalidInputException.class)
  public ResponseEntity<Object> handlerInvalidInputException(InvalidInputException ex)
  {
    Map<String, Object> body = new HashMap<>();

    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    body.put("error", "Valor incorrecto");
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
