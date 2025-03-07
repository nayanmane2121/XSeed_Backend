package com.tcognition.recruiterservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcognition.recruiterservice.entity.CompanyClientEntity;
import com.tcognition.recruiterservice.entity.CompanyEntity;
import com.tcognition.recruiterservice.entity.PayRateEntity;
import java.util.List;


public interface PayrateRepository extends JpaRepository<PayRateEntity,Long>{

	public List<PayRateEntity> findByCompany(CompanyEntity company);
}
