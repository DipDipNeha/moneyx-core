/**
 * @author Dipak
 */

package com.pscs.moneyx.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pscs.moneyx.entity.MoneyXCoreCustomer;

public interface MoneyXCoreCustomerRepo extends JpaRepository<MoneyXCoreCustomer, Long> {

	MoneyXCoreCustomer  findByEmail(String email);
	MoneyXCoreCustomer findByEmailAndPassword(String email, String password);
	
}
