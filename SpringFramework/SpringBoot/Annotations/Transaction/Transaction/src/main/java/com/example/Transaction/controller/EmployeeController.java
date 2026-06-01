
package com.example.Transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Transaction.entity.Employee;
import com.example.Transaction.service.EmployeeService;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/employee")
	public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
		try {
			Employee savedEmployee = employeeService.addEmpolyee(employee);
			return ResponseEntity.ok(savedEmployee);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Transaction failed " + e.getMessage());
		}
	}

}
