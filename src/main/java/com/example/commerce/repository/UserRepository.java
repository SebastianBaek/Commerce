package com.example.commerce.repository;

import com.example.commerce.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  Optional<User> findByEmail(String email);

  Optional<User> findByEmailAndUsername(String email, String username);
}
