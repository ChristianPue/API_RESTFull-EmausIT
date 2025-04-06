package com.emausit.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emausit.api.entity.User;
import com.emausit.api.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController 
{
  @Autowired
  private UserService userService;

  @GetMapping
  public ResponseEntity<List<User>> getUsers()
  {
    return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user)
  {
    return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") Integer id)
  {
    return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<User> updateUserById(@PathVariable("id") Integer id, @RequestBody User user)
  {
    return new ResponseEntity<>(userService.updateUserById(id, user), HttpStatus.OK);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<String> deleteUserById(@PathVariable("id") Integer id)
  {
    return new ResponseEntity<>(userService.deleteUserById(id), HttpStatus.OK);
  }
}
