package com.tcognition.candidate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcognition.candidate.collections.CandidateCollection;
import com.tcognition.candidate.collections.EducationalDetailsCollection;
import com.tcognition.candidate.collections.SocialDetailsCollection;
import com.tcognition.candidate.dto.ProfessionalDetailsDTO;
import com.tcognition.candidate.service.CandidateService;
import com.tcognition.candidate.utils.AppConstants;

@RestController
@RequestMapping(AppConstants.CANDIDATE_REQUEST_MAPPING)
public class CandidateController {

	@Autowired
	private CandidateService candidateService;

	private static final Logger logger = LoggerFactory.getLogger(CandidateController.class);

	@GetMapping(AppConstants.GET_ALL_CANDIDATES_URL)
	public ResponseEntity<?> getAllCandidates() {
		logger.info("Inside Candidate Controller");
		logger.info("Inside getAllCandidates API");

		return ResponseEntity.ok(candidateService.getAllCandidates());
	}

	@GetMapping(AppConstants.GET_CANDIDATES_DETAILS_URL)
	public ResponseEntity<?> getCandidateDetails(@RequestParam Long id) {
		logger.info("Inside Candidate Controller");
		logger.info("Inside getCandidateDetails API");

		return ResponseEntity.ok(candidateService.getCandidateDetails(id));
	}

	@PostMapping(AppConstants.REGISTER_CANDIDATE_URL)
	public ResponseEntity<?> registerCandidate(@RequestBody CandidateCollection candidate) {
		logger.info("Inside Candidate Controller");
		logger.info("Inside registerCandidate API");

		return ResponseEntity.ok(candidateService.registerCandidate(candidate));
	}

	@PostMapping(AppConstants.SAVE_PROFESSIONAL_DETAILS_URL)
	public ResponseEntity<?> saveProfessionalDetails(@RequestBody ProfessionalDetailsDTO professionalDetailsDTO) {
		logger.info("Inside Candidate Controller");
		logger.info("Inside saveProfessionalDetails API");

		return ResponseEntity.ok(candidateService.saveProfessionalDetails(professionalDetailsDTO));
	}

	@PostMapping(AppConstants.GET_PROFESSIONAL_DETAILS_URL)
	public ResponseEntity<?> getProfessionalDetails(@RequestParam Long candidateId) {
		logger.info("Inside Candidate Controller");
		logger.info("Inside getProfessionalDetails API");

		return ResponseEntity.ok(candidateService.getProfessionalDetails(candidateId));
	}

	@PostMapping(AppConstants.SAVE_EDUCATIONAL_DETAILS_URL)
	public ResponseEntity<?> saveEducationalDetails(@RequestBody EducationalDetailsCollection educationalDetails) {
		logger.info("Inside Candidate Controller");
		logger.info("Inside saveEducationalDetails API");

		return ResponseEntity.ok(candidateService.saveEducationalDetails(educationalDetails));
	}

	@PostMapping(AppConstants.GET_EDUCATIONAL_DETAILS_URL)
	public ResponseEntity<?> getEducationalDetails(@RequestParam Long candidateId) {
		logger.info("Inside Candidate Controller");
		logger.info("Inside getEducationalDetails API");

		return ResponseEntity.ok(candidateService.getEducationalDetails(candidateId));
	}

	@PostMapping(AppConstants.SAVE_SOCIAL_DETAILS_URL)
	public ResponseEntity<?> saveSocialDetails(@RequestBody SocialDetailsCollection socialDetails) {
		logger.info("Inside Candidate Controller");
		logger.info("Inside saveSocialDetails API");

		return ResponseEntity.ok(candidateService.saveSocialDetails(socialDetails));
	}

	@PostMapping(AppConstants.GET_SOCIAL_DETAILS_URL)
	public ResponseEntity<?> getSocialDetails(@RequestParam Long candidateId) {
		logger.info("Inside Candidate Controller");
		logger.info("Inside getSocialDetails API");

		return ResponseEntity.ok(candidateService.getSocialDetails(candidateId));
	}

}
