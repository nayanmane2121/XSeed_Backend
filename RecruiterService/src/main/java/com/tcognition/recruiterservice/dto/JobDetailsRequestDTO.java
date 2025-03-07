package com.tcognition.recruiterservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobDetailsRequestDTO {

	@NotNull(message = "Job Id must not be blank")
	private Long jobId;
}
