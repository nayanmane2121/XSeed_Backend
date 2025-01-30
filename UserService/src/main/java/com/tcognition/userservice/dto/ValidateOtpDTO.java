package com.tcognition.userservice.dto;

import lombok.Data;

@Data
public class ValidateOtpDTO {

	private String email;
	private String otp;
}
