package com.example.basicAuthentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.basicAuthentication.entity.AuthRequest;
import com.example.basicAuthentication.util.JWTUtil;

@RestController
public class AuthController{

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTUtil jwtutil;

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest){
        try{
            authenticationManager.authenticate(
                // UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken implements Authentication
                // So basically we are creating object of Authentication
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            return jwtutil.generateToken(authRequest.getUsername());
        }
        catch(Exception e){
            throw e;
        }
    }
}
