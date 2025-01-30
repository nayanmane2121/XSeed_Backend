package com.tcognition.userservice.service;

import org.springframework.web.bind.annotation.RequestBody;

import com.tcognition.userservice.dto.CreateSuperAdminRequestDTO;
import com.tcognition.userservice.dto.ResponseDTO;

public interface SuperAdminService {

	public ResponseDTO createSuperAdmin(CreateSuperAdminRequestDTO createSuperAdminRequestDTO);
}
