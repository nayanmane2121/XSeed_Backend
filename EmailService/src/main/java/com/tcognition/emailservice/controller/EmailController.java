package com.tcognition.emailservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tcognition.emailservice.dto.EmailRequestDTO;
import com.tcognition.emailservice.service.EmailService;
import com.tcognition.emailservice.utils.AppConstants;
import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.EMAIL_REQUEST_MAPPING)
public class EmailController {

	@Autowired
	private EmailService emailService;

	private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

	@PostMapping(value = AppConstants.SEND_EMAIL_URL)
	public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequestDTO request) {

		logger.info("Inside Email controller");
		logger.info("Inside sendEmail API");
		return ResponseEntity.ok(emailService.sendEmail(request));
	}

	@PostMapping(value = AppConstants.SEND_EMAIL_WITH_ATTACHMENT_URL)
	public ResponseEntity<String> sendEmailWithAttachment(@RequestBody EmailRequestDTO emailRequestDTO) {

		logger.info("Inside Email controller");
		logger.info("Inside sendEmailWithAttachment API");
		return ResponseEntity.ok(emailService.sendEmailWithAttachments(emailRequestDTO));
	}

	@PostMapping(value = "/ping")
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok("Email service is working!");
	}
}
