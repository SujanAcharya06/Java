package com.example.JwtAuthentication.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.JwtAuthentication.dtos.LoginResponse;
import com.example.JwtAuthentication.dtos.LoginUserDto;
import com.example.JwtAuthentication.dtos.RegisterUserDto;
import com.example.JwtAuthentication.entities.Users;
import com.example.JwtAuthentication.services.AuthenticationService;
import com.example.JwtAuthentication.services.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController{
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Users> register(@RequestBody RegisterUserDto registerUserDto){
        // System.out.println("email: " + registerUserDto.getEmail());
        // System.out.println("fullName: " + registerUserDto.getFullName());
        Users registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto){
        Users authenticated = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticated);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

}
