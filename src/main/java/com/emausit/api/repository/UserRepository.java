package com.emausit.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.emausit.api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> 
{
  @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
  boolean existsByEmail(String email);

  @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
  boolean existsByUsername(String username);

  @Query("SELECT u FROM User u WHERE u.email = :email")
  Optional<User> findByEmail(String email);
  
  @Query("SELECT u FROM User u WHERE u.username = :username")
  Optional<User> findByUsername(String username);
}