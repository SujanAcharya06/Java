package com.example.jobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jobportal.model.User;
import com.example.jobportal.repo.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

	public User saveUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		System.out.println(user.getPassword());
		return userRepo.save(user);
	}

}
