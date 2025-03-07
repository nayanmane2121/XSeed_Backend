package com.tcognition.recruiterservice.controller;

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

import com.tcognition.recruiterservice.service.CompanyService;
import com.tcognition.recruiterservice.utils.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.COMPANY_REQUEST_MAPPING)
public class CompanyController {
	
private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
	
	@Autowired
	CompanyService companyService;
	
	@GetMapping(value = AppConstants.GET_ALL_COMPANIES_URL)
	public ResponseEntity<?> getAllCompanies() {

		logger.info("Inside Company Controller");
		logger.info("Inside getAllCompanies API");
		return ResponseEntity.ok(companyService.getAllCompanies());

	}

	@PostMapping(value = AppConstants.GET_COMPANY_DETAILS_URL)
	public ResponseEntity<?> getCompanyDetails(@Valid @RequestParam Long companyId) {

		logger.info("Inside Company Controller");
		logger.info("Inside getCompanyDetails API");
		return ResponseEntity.ok(companyService.getCompanyDetails(companyId));

	}

}
