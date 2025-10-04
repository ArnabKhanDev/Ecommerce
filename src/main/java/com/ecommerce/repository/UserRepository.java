package com.ecommerce.repository;

import com.ecommerce.enums.AppRole;
import com.ecommerce.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserName(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = :role")
    Page<User> findByRoleName(@Param("role") AppRole role, Pageable pageable);
}
