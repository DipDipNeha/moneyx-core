package com.pscs.moneyx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pscs.moneyx.entity.CustomerDocInfo;
@Repository
public interface CustomerDocInfoRepo extends JpaRepository<CustomerDocInfo, Long>{

}
