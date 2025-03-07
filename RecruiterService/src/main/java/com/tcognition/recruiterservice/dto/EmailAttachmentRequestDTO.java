package com.tcognition.recruiterservice.dto;



import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class EmailAttachmentRequestDTO {

	private EmailRequestDTO emailRequest;
	private MultipartFile[] files;
}
