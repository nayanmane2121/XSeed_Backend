package com.tcognition.recruiterservice.service;

import org.springframework.web.multipart.MultipartFile;

import com.tcognition.recruiterservice.dto.InterviewDetailsDTO;
import com.tcognition.recruiterservice.dto.JobDetailsRequestDTO;
import com.tcognition.recruiterservice.dto.ResponseDTO;

public interface InterviewService {

	ResponseDTO getinterviewdetails(JobDetailsRequestDTO jobDetailsRequestDTO);
	ResponseDTO scheduleInterview (InterviewDetailsDTO scheduleInterviewRequestDTO, MultipartFile JDFile, MultipartFile CVFile);
}
