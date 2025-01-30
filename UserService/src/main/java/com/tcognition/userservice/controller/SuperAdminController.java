package com.tcognition.userservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcognition.userservice.dto.CreateSuperAdminRequestDTO;
import com.tcognition.userservice.service.SuperAdminService;
import com.tcognition.userservice.utils.AppConstants;

@RestController
@RequestMapping(AppConstants.SUPERADMIN_REQUEST_MAPPING)
public class SuperAdminController {

	@Autowired
	SuperAdminService superAdminService;
	
	private static final Logger logger = LoggerFactory.getLogger(SuperAdminController.class);

	@PostMapping(value = AppConstants.CREATE_SUPERADMIN_URL)
	public ResponseEntity<?> createSuperAdmin(@RequestBody CreateSuperAdminRequestDTO createSuperAdminRequestDTO) {

		logger.info("Inside Super Admin controller");
		logger.info("Inside Create SuperAdmin API");
		return ResponseEntity.ok(superAdminService.createSuperAdmin(createSuperAdminRequestDTO));
	}
}
