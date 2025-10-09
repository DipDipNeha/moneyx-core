/**
 * @author Dipak
 */

package com.pscs.moneyx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pscs.moneyx.entity.MoneyXCoreCustomer;
@Repository
public interface MoneyXCoreCustomerRepo extends JpaRepository<MoneyXCoreCustomer, Long> {

	MoneyXCoreCustomer  findByEmail(String email);
	MoneyXCoreCustomer findByEmailAndPassword(String email, String password);
	
}
