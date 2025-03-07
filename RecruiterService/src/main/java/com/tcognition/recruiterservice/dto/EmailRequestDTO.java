package com.tcognition.recruiterservice.dto;

import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailRequestDTO {

	@Email(message = "Invalid email format")
    @NotBlank(message = "Recipient email is required")
    private String to;
	private String[] cc;
    private String subject;
    private String message;
    private String templateName;
    private String name;
    private Map<String, String> data;
}
