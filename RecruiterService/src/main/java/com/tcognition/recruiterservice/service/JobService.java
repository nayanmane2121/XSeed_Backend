package com.tcognition.recruiterservice.service;

import com.tcognition.recruiterservice.dto.JobDetailsDTO;
import com.tcognition.recruiterservice.dto.JobDetailsFilterDTO;
import com.tcognition.recruiterservice.dto.JobDetailsRequestDTO;
import com.tcognition.recruiterservice.dto.ResponseDTO;

public interface JobService {

	public ResponseDTO getJobDetails(JobDetailsRequestDTO jobDetailsRequestDTO);
	public ResponseDTO saveJobDetails(JobDetailsDTO jobDetailsDTO);
	public ResponseDTO getFilteredJobDetails(JobDetailsFilterDTO jobDetailsFilterDTO);
}
