package com.tcognition.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcognition.userservice.entity.UserEntity;
import com.tcognition.userservice.entity.UserRoleMappingEntity;


public interface UserRoleMappingRepository extends JpaRepository<UserRoleMappingEntity, Long> {

	UserRoleMappingEntity findByUser(UserEntity user);
}
