package com.tcognition.recruiterservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tcognition.recruiterservice.entity.JobDetailsEntity;

public interface JobDetailsRepository extends JpaRepository<JobDetailsEntity, Long>, JpaSpecificationExecutor<JobDetailsEntity>{

}
