package com.example.cachingdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CachingdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachingdemoApplication.class, args);
	}

}
