package com.example.JwtAuthentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.JwtAuthentication.entities.Users;

@Repository
public interface UserRepository extends CrudRepository<Users, Long>{
    Optional<Users> findByEmail(String email);
}
