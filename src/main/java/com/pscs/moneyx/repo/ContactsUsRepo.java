/**
 * @author Dipak
 */

package com.pscs.moneyx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pscs.moneyx.entity.ContactsUs;
@Repository
public interface ContactsUsRepo extends JpaRepository<ContactsUs, Long> {
	

}
