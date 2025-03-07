package com.tcognition.recruiterservice.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcognition.recruiterservice.dto.InterviewDetailsDTO;
import com.tcognition.recruiterservice.dto.JobDetailsRequestDTO;
import com.tcognition.recruiterservice.service.InterviewService;
import com.tcognition.recruiterservice.utils.AppConstants;
import com.tcognition.recruiterservice.utils.MessageConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.INTERVIEW_REQUEST_MAPPING)
public class InterviewController {

	private static final Logger logger = LoggerFactory.getLogger(InterviewController.class);

	@Autowired
	InterviewService interviewService;

	@PostMapping(value = AppConstants.GET_INTERVIW_DETAILS_URL)
	public ResponseEntity<?> getinterviewdetails(
			@Valid @RequestBody JobDetailsRequestDTO JobDetailsRequestDTO) {

		logger.info("Inside Interview Controller");
		logger.info("Inside getinterviewdetails API");
		return ResponseEntity.ok(interviewService.getinterviewdetails(JobDetailsRequestDTO));

	}

	@PostMapping(value = AppConstants.SCHEDULE_INTERVIEW_URL)
	public ResponseEntity<?> scheduleInterview(
	    @RequestParam("interviewDetailsDTO") String interviewDetailsDTO, 
	    @RequestParam("JDFile") MultipartFile JDFile, 
	    @RequestParam("CVFile") MultipartFile CVFile) {

	    logger.info("Inside Interview Controller");
	    logger.info("Inside scheduleInterview API");

	    // Convert the stringified JSON into InterviewDetailsDTO
	    ObjectMapper objectMapper = new ObjectMapper();
	    InterviewDetailsDTO interviewDetails;
	    try {
	        interviewDetails = objectMapper.readValue(interviewDetailsDTO, InterviewDetailsDTO.class);
	    } catch (IOException e) {
	        logger.error("Error parsing JSON", e);
	        return ResponseEntity.badRequest().body(MessageConstants.INVALID_JSON_FORMAT);
	    }

	    // Call the service method to handle the interview scheduling logic
	    return ResponseEntity.ok(interviewService.scheduleInterview(interviewDetails, JDFile, CVFile));
	}
}
