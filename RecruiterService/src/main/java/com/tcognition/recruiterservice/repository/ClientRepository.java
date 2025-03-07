package com.tcognition.recruiterservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcognition.recruiterservice.entity.CompanyClientEntity;
import com.tcognition.recruiterservice.entity.CompanyEntity;

public interface ClientRepository extends JpaRepository<CompanyClientEntity, Long> {

	public List<CompanyClientEntity> findByCompany(CompanyEntity company);
	public Optional<CompanyClientEntity> findById(Long clientId);

}
