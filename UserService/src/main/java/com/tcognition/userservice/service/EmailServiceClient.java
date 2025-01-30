package com.tcognition.userservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.tcognition.userservice.config.FeignConfig;
import com.tcognition.userservice.dto.EmailRequestDTO;


@FeignClient(name = "email", configuration = FeignConfig.class)
public interface EmailServiceClient {

	@PostMapping("email/api/send")
    String sendEmail( @RequestBody EmailRequestDTO emailRequest);
}
