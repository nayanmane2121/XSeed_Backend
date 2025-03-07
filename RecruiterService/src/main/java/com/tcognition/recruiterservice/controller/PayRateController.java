package com.tcognition.recruiterservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcognition.recruiterservice.service.ClientService;
import com.tcognition.recruiterservice.service.PayrateService;
import com.tcognition.recruiterservice.utils.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.PAYRATE_REQUEST_MAPPING)
public class PayRateController {

	private static final Logger logger = LoggerFactory.getLogger(PayRateController.class);

	@Autowired
	PayrateService payrateService;
	
	@PostMapping(value = AppConstants.GET_PAYRATE_DETAILS_URL)
	public ResponseEntity<?> getCompanyDetails(@Valid @RequestParam Long companyId) {

		logger.info("Inside Company Controller");
		logger.info("Inside getCompanyDetails API");
		return ResponseEntity.ok(payrateService.getPayRateDetails(companyId));

	}

}
