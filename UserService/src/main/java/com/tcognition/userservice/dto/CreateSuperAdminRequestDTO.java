package com.tcognition.userservice.dto;

import lombok.Data;

@Data
public class CreateSuperAdminRequestDTO {

	private String firstName;
	private String lastName;
	private String email;
	private String contact;
	private String password;
	
}
