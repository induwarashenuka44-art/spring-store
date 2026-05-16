package com.induwara.store.repositories;

import com.induwara.store.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email);
}
