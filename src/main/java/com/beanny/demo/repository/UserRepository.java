package com.beanny.demo.repository;

import com.beanny.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Boolean existsByName(String username);
    Boolean existsByEmail(String email);
    Optional<User> findByName(String username);
    Optional<User> findByEmail(String email);
}
