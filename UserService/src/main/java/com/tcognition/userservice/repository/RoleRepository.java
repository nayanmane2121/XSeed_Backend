package com.tcognition.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcognition.userservice.entity.RoleEntity;


public interface RoleRepository  extends JpaRepository<RoleEntity, Long>{
}
