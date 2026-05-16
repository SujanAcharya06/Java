package com.example.basicAuthentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.basicAuthentication.entity.Users;

@Repository
public interface UserDetailsRepository extends JpaRepository<Users, Long>{
    Optional<Users> findByUsername(String username);
}
