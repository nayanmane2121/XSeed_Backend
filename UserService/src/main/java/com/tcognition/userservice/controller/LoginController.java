package com.tcognition.userservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcognition.userservice.dto.LoginRequestDTO;
import com.tcognition.userservice.dto.ResetPasswordRequestDTO;
import com.tcognition.userservice.dto.ValidateOtpDTO;
import com.tcognition.userservice.service.LoginService;
import com.tcognition.userservice.utils.AppConstants;

import jakarta.validation.Valid;


@RestController
@RequestMapping(AppConstants.AUTH_REQUEST_MAPPING)
public class LoginController {
	
	@Autowired
	LoginService loginService;
	
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@PostMapping(value = AppConstants.LOGIN_URL)
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
		
		logger.info("Inside Login controller");
		logger.info("Inside Login API");
		return ResponseEntity.ok(loginService.login(loginRequest));
				
	}
	
	
	@PostMapping(value = AppConstants.RESET_PASSWORD_URL)
	public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO) {
		
		logger.info("Inside Login controller");
		logger.info("Inside resetPassword API");
		return ResponseEntity.ok(loginService.resetPassword(resetPasswordRequestDTO));
				
	}
	
	@PostMapping(value = AppConstants.VALIDATE_OTP_URL)
	public ResponseEntity<?> validateOTP(@Valid @RequestBody ValidateOtpDTO validateOtpDTO) {
		
		logger.info("Inside Login controller");
		logger.info("Inside validateOTP API");
		return ResponseEntity.ok(loginService.validateOTP(validateOtpDTO));
				
	}
	
	
	@GetMapping(value = "test")
	public String test() {
		
		return "Hey...";
	}
}
