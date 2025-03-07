package com.tcognition.userservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.tcognition.userservice.config.FeignConfig;
import com.tcognition.userservice.dto.EmailAttachmentRequestDTO;
import com.tcognition.userservice.dto.EmailRequestDTO;


@FeignClient(name = "email", configuration = FeignConfig.class)
public interface EmailServiceClient {

	@PostMapping("email/api/sendemail")
    String sendEmail( @RequestBody EmailRequestDTO emailRequest);
	
	@PostMapping("email/api/sendemailwithattachment")
    String sendEmailWithAttachment( @RequestBody EmailAttachmentRequestDTO emailAttachmentRequestDTO);
}
