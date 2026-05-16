package com.example.JwtAuthentication.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.JwtAuthentication.entities.Users;
import com.example.JwtAuthentication.services.UserService;


@RequestMapping("/users")
@RestController
public class UserController{
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // @GetMapping("/me")
    // public ResponseEntity<Users> authenticatedUser(){
    //     System.out.println(">>>> /me endpoint called");
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //
    //     Users currentUser = (Users)authentication.getPrincipal();
    //     System.out.println("******currentUser*******: " + authentication.getPrincipal());
    //
    //     return ResponseEntity.ok(currentUser);
    // }
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> authenticatedUser() {
        System.out.println(">>>> /me endpoint called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Users currentUser = (Users) authentication.getPrincipal();
        System.out.println("******currentUser*******: " + authentication.getPrincipal());

        // Create a clean map with only the fields you want to return
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", currentUser.getId());
        userResponse.put("email", currentUser.getEmail());
        userResponse.put("fullName", currentUser.getFullName());
        userResponse.put("createdAt", currentUser.getCreatedAt());
        userResponse.put("updatedAt", currentUser.getUpdatedAt());

        return ResponseEntity.ok(userResponse);
    }
    // In the above code, we retrieve the authenticated user from the security
    // context that has been set in the file JwtAuthenticationFilter.java

    @GetMapping("/")
    public ResponseEntity<List<Users>> allUsers(){
        List<Users> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }
}
