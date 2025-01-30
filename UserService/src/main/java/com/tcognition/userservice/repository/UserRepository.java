package com.tcognition.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tcognition.userservice.entity.UserEntity;
import com.tcognition.userservice.utils.SQLConstants;

public interface UserRepository extends JpaRepository<UserEntity, Long >{

	@Query(SQLConstants.QUERY_FIND_USER_BY_EMAIL_OR_CONTACT)
	Optional<UserEntity> findByEmailOrContactIgnoreCase(String userContact);
}
