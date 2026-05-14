
package com.example.Transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Transaction.entity.Address;
import com.example.Transaction.entity.Employee;
import com.example.Transaction.repo.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private AddressService addressService;

	@Transactional(rollbackFor = Exception.class)
	public Employee addEmpolyee(Employee employee) {
		log.info("Employee: {}", employee);
		Employee employeeSavedToDB = employeeRepository.save(employee);

		// Address address = new Address();
		Address address = null;
		address.setId(123L);
		address.setAddress("London");
		address.setEmployee(employee);

		// This may throw an exception intentionally for testing rollback
		if (employee.getName().equalsIgnoreCase("error")) {
			throw new RuntimeException("Simulated Exception: Forcing rollback!");
		}

		this.addressService.addAddress(address);
		log.info("Address: {}", address);
		log.info("Employee details saved successfully");
		return employeeSavedToDB;
	}
}
