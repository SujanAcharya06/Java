package com.example.basicAuthentication.service;

import com.example.basicAuthentication.entity.Users;
import com.example.basicAuthentication.repository.UserDetailsRepository;
import org.apache.catalina.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer {

    @Bean
    public CommandLineRunner createAdminUser(UserDetailsRepository userDetailsRepository, PasswordEncoder passwordEncoder){
        return args -> {
            if(userDetailsRepository.findByUsername("admin").isEmpty()){
                Users user = new Users();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setRole("ADMIN");

                userDetailsRepository.save(user);
                System.out.println("Default admin user created");
            }
        };
    }
}
