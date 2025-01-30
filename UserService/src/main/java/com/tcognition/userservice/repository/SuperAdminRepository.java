package com.tcognition.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcognition.userservice.entity.SuperAdminEntity;

public interface SuperAdminRepository extends JpaRepository<SuperAdminEntity, Long>{

}
