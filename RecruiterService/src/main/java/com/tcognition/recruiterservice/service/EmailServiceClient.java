package com.tcognition.recruiterservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.tcognition.recruiterservice.dto.EmailRequestDTO;
import com.tcognition.recruiterservice.config.FeignConfig;

@Service
@FeignClient(name = "email", configuration = FeignConfig.class)
public interface EmailServiceClient {

	@PostMapping("email/api/sendemail")
	String sendEmail(@RequestBody EmailRequestDTO emailRequest);

	@PostMapping(value = "email/api/sendemailwithattachment")
	void sendEmailWithAttachment(@RequestBody EmailRequestDTO emailRequest);
}
