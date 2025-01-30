package com.tcognition.userservice.dto;

import lombok.Data;

@Data
public class JwtClaimsDTO {

	String userRole;
	String userName;
	Long userId;
	
}
