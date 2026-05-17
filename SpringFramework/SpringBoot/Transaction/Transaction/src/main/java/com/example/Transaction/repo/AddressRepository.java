
package com.example.Transaction.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Transaction.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
