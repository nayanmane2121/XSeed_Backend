package com.tcognition.recruiterservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcognition.recruiterservice.entity.CompanyEntity;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

	public List<CompanyEntity> findAll();
	public Optional <CompanyEntity> findById(Long id);
}
