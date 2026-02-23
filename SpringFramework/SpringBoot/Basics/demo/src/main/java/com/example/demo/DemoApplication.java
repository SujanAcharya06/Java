package com.example.demo;

import com.example.demo.service.LaptopService;
import com.example.demo.model.Laptop;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DemoApplication.class, args);


		LaptopService service = context.getBean(LaptopService.class);

		Laptop lap =  context.getBean(Laptop.class);

		// job of service is to do any processing with the laptop
		service.addLaptop(lap);


//		Alien obj = context.getBean(Alien.class);
//		System.out.println(obj.getAge());
//		obj.code();
	}
}
