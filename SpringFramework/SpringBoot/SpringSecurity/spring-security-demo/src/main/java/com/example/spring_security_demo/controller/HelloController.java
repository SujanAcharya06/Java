package com.example.spring_security_demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

	@GetMapping("hello")
	public String greet(HttpServletRequest request) {
		return "Hello: " + request.getSession().getId();
	}

	@GetMapping("about")
	public String about(HttpServletRequest request) {
		return "About: " + request.getSession().getId();
	}

}
