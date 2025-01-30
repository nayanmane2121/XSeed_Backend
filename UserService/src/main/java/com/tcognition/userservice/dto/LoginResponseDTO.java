package com.tcognition.userservice.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {

	private String token;

	private User user;

	@Data
	public static class User {
		private Long id;
		private String name;
		private String email;
		private String userRole;
	}

}