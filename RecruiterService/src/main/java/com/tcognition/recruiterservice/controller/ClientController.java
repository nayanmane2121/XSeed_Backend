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

import com.tcognition.recruiterservice.service.ClientService;
import com.tcognition.recruiterservice.utils.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.CLIENT_REQUEST_MAPPING)
public class ClientController {

	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

	@Autowired
	ClientService clientService;

	@PostMapping(value = AppConstants.GET_ALL_CLIENTS_URL)
	public ResponseEntity<?> getAllClients(@Valid @RequestParam Long companyId) {

		logger.info("Inside Client Controller");
		logger.info("Inside getAllClients API");
		return ResponseEntity.ok(clientService.getAllClients(companyId));

	}

	@PostMapping(value = AppConstants.GET_CLIENT_DETAILS_URL)
	public ResponseEntity<?> getJobDetails(@Valid @RequestParam Long clientId) {

		logger.info("Inside Client Controller");
		logger.info("Inside getAllClients API");
		return ResponseEntity.ok(clientService.getClientDetails(clientId));

	}
}
