package com.tcognition.userservice.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcognition.userservice.dto.CreateSuperAdminRequestDTO;
import com.tcognition.userservice.dto.ResponseDTO;
import com.tcognition.userservice.entity.SuperAdminEntity;
import com.tcognition.userservice.entity.UserEntity;
import com.tcognition.userservice.entity.UserRoleMappingEntity;
import com.tcognition.userservice.repository.RoleRepository;
import com.tcognition.userservice.repository.SuperAdminRepository;
import com.tcognition.userservice.repository.UserRepository;
import com.tcognition.userservice.repository.UserRoleMappingRepository;
import com.tcognition.userservice.service.SuperAdminService;
import com.tcognition.userservice.utils.ErrorMessagesConstants;
import com.tcognition.userservice.utils.MessageConstants;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

	@Autowired
	SuperAdminRepository superAdminRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleMappingRepository userRoleMappingRepository;

	@Autowired
	RoleRepository roleRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(SuperAdminServiceImpl.class);

	@Override
	public ResponseDTO createSuperAdmin(CreateSuperAdminRequestDTO createSuperAdminRequestDTO) {

		logger.info("Inside createSuperAdmin().");
		logger.info("Starting the process to create a new Super Admin.");

		try {
		    // Configure ObjectMapper once and reuse
		    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		    // Map DTO to SuperAdminEntity and set additional fields
		    SuperAdminEntity superAdminEntity = objectMapper.convertValue(createSuperAdminRequestDTO, SuperAdminEntity.class);
		    superAdminEntity.setActive(true);
		    superAdminEntity.setDeleted(false);

		    // Save SuperAdminEntity to the database
		    logger.info("Saving SuperAdminEntity to the database.");
		    superAdminEntity = superAdminRepository.save(superAdminEntity);

		    // Map SuperAdminEntity to UserEntity
		    UserEntity userEntity = objectMapper.convertValue(superAdminEntity, UserEntity.class);
		    userEntity.setName(String.format("%s %s", superAdminEntity.getFirstName(), superAdminEntity.getLastName())); // Concatenate name fields

		    // Save UserEntity to the database
		    logger.info("Saving UserEntity to the database.");
		    userEntity = userRepository.save(userEntity);

		    // Create and save UserRoleMappingEntity
		    logger.info("Creating and saving UserRoleMappingEntity.");
		    UserRoleMappingEntity userRoleMappingEntity = new UserRoleMappingEntity();
		    userRoleMappingEntity.setUser(userEntity);
		    userRoleMappingEntity.setRole(roleRepository.findById(1L).get());

		    userRoleMappingRepository.save(userRoleMappingEntity);

		    logger.info("Super Admin creation process completed successfully.");
		    return ResponseDTO.success(MessageConstants.SUPER_ADMIN_CREATED_SUCCESSFULLY);
		} catch (Exception e) {
		    logger.error(ErrorMessagesConstants.ERROR_CREATING_SUPER_ADMIN, e);
		    return ResponseDTO.success(ErrorMessagesConstants.ERROR_CREATING_SUPER_ADMIN);
		}

	}

}
