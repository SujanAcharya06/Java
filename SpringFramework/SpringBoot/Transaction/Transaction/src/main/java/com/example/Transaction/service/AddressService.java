
package com.example.Transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Transaction.entity.Address;
import com.example.Transaction.repo.AddressRepository;

@Service
public class AddressService {

	@Autowired
	private AddressRepository addressRepository;

	public Address addAddress(Address address) {
		return this.addressRepository.save(address);

	}

}
