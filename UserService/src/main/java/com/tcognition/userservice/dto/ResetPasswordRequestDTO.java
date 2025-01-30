package com.tcognition.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {

	@NotBlank(message = "User contact must not be blank")
	private String userContact;
	
	@NotBlank(message = "Mode must not be blank")
	private String mode;
	
}
