package com.tcognition.recruiterservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcognition.recruiterservice.service.JobService;
import com.tcognition.recruiterservice.dto.JobDetailsDTO;
import com.tcognition.recruiterservice.dto.JobDetailsFilterDTO;
import com.tcognition.recruiterservice.dto.JobDetailsRequestDTO;
import com.tcognition.recruiterservice.utils.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.JOB_REQUEST_MAPPING)
public class JobController {

	private static final Logger logger = LoggerFactory.getLogger(JobController.class);

	@Autowired
	JobService jobService;

	@PostMapping(value = AppConstants.GET_JOB_DETAILS_URL)
	public ResponseEntity<?> getJobDetails(@Valid @RequestBody JobDetailsRequestDTO jobDetailsRequestDTO) {

		logger.info("Inside Job Controller");
		logger.info("Inside getjobdetails API");
		return ResponseEntity.ok(jobService.getJobDetails(jobDetailsRequestDTO));

	}

	@PostMapping(value = AppConstants.SAVE_JOB_DETAILS_URL)
	public ResponseEntity<?> saveJobDetails(@RequestBody JobDetailsDTO jobDetailsDTO) {

		logger.info("Inside Job Controller");
		logger.info("Inside savejobdetails API");
		return ResponseEntity.ok(jobService.saveJobDetails(jobDetailsDTO));

	}

	@PostMapping(value = AppConstants.GET_FILTERED_JOB_DETAILS_URL)
	public ResponseEntity<?> getFilteredJobDetails(@RequestBody JobDetailsFilterDTO jobDetailsFilterDTO) {

		logger.info("Inside Job Controller");
		logger.info("Inside getfilteredjobdetails API");
		return ResponseEntity.ok(jobService.getFilteredJobDetails(jobDetailsFilterDTO));

	}
}
