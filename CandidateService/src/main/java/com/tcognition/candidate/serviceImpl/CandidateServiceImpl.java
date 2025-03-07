package com.tcognition.candidate.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcognition.candidate.collections.CandidateCollection;
import com.tcognition.candidate.collections.EducationalDetailsCollection;
import com.tcognition.candidate.collections.ProfessionalDetailsCollection;
import com.tcognition.candidate.collections.ProfessionalExperienceCollection;
import com.tcognition.candidate.collections.SocialDetailsCollection;
import com.tcognition.candidate.dto.ProfessionalDetailsDTO;
import com.tcognition.candidate.dto.ResponseDTO;
import com.tcognition.candidate.repository.CandidateRepository;
import com.tcognition.candidate.repository.EducationalDetailsRepository;
import com.tcognition.candidate.repository.ProfessionalDetailsRepository;
import com.tcognition.candidate.repository.ProfessionalExperienceRepository;
import com.tcognition.candidate.repository.SocialDetailsRepository;
import com.tcognition.candidate.service.CandidateService;
import com.tcognition.candidate.utils.ErrorMessagesConstants;
import com.tcognition.candidate.utils.MessageConstants;

@Service
public class CandidateServiceImpl implements CandidateService {

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	ProfessionalDetailsRepository professionalDetailsRepository;

	@Autowired
	ProfessionalExperienceRepository professionalExperienceRepository;

	@Autowired
	EducationalDetailsRepository educationalDetailsRepository;

	@Autowired
	SocialDetailsRepository socialDetailsRepository;
	
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

	private static final Logger logger = LoggerFactory.getLogger(CandidateServiceImpl.class);

	@Override
	public ResponseDTO getAllCandidates() {

		logger.info("Inside getAllCandidates()");

		List<CandidateCollection> candidateList;
		try {
			logger.info("Fetching candidate list...");
			candidateList = candidateRepository.findAll();

			if (candidateList == null || candidateList.isEmpty()) {

				ResponseDTO.error(ErrorMessagesConstants.ERROR_NO_CANDIDATE_PRESENT_IN_SYSTEM);
			}

		} catch (Exception e) {
			logger.error("Error while fetching candidate list.", e);
			return ResponseDTO.error(ErrorMessagesConstants.ERROR_FETCHING_CANDIDATE_LIST);
		}

		return ResponseDTO.success(candidateList);
	}

	@Override
	public ResponseDTO getCandidateDetails(Long id) {

		logger.info("Inside getCandidateDetails()");

		CandidateCollection candidate = null;
		try {
			logger.info("Fetching candidate for Id{}:" + id);
			candidate = candidateRepository.findById(id).get();

			if (candidate == null) {
				ResponseDTO.error(ErrorMessagesConstants.ERROR_NO_CANDIDATE_PRESENT_IN_SYSTEM + "with id {}:" + id);
			}

		} catch (Exception e) {
			logger.error("Error while fetching candidate details with id {}:" + id, e);
			return ResponseDTO.error(ErrorMessagesConstants.ERROR_FETCHING_CANDIDATE_DETAILS);
		}

		return ResponseDTO.success(candidate);
	}

	@Override
	public ResponseDTO registerCandidate(CandidateCollection candidate) {

		logger.info("Inside registerCandidate()");

		try {
			logger.info("Registering the Candidate :" + candidate.getFirstName() + " " + candidate.getLastName());
			if (candidate.getId() == null || candidate.getId().equals("")) {
				candidate.setId(sequenceGeneratorService.generateSequence(CandidateCollection.SEQUENCE_NAME));
			}
			return ResponseDTO.success(candidateRepository.save(candidate));
		} catch (Exception e) {
			logger.error(
					"Error while registering the candidate " + candidate.getFirstName() + " " + candidate.getLastName(),
					e);
			return ResponseDTO.error(ErrorMessagesConstants.ERROR_REGISTERING_CANDIDATE);
		}
	}

	@Override
	public ResponseDTO saveProfessionalDetails(ProfessionalDetailsDTO professionalDetailsDTO) {

		logger.info("Inside saveProfessionalDetails()");
		try {
			logger.info("Saving the Professional Details for candidate id :"
					+ professionalDetailsDTO.getProfessionalDetails().getCandidateId());

			professionalDetailsRepository.save(professionalDetailsDTO.getProfessionalDetails());

			logger.info("Saving the Professional Experience for candidate id :"
					+ professionalDetailsDTO.getProfessionalDetails().getCandidateId());

			for (ProfessionalExperienceCollection professionalExperience : professionalDetailsDTO
					.getProfessionalExperiences()) {
				professionalExperienceRepository.save(professionalExperience);
			}

			return ResponseDTO.success(MessageConstants.PROFESSIONAL_DETAILS_SAVED);
		} catch (Exception e) {
			logger.error("Error while saving the professional details for candidate Id "
					+ professionalDetailsDTO.getProfessionalDetails().getCandidateId(), e);
			return ResponseDTO.error(ErrorMessagesConstants.ERROR_SAVING_PROFESSIONAL_DETAILS);
		}
	}

	@Override
	public ResponseDTO getProfessionalDetails(Long candidateId) {

		logger.info("Inside getProfessionalDetails()");
		ProfessionalDetailsDTO professionalDetailsDTO = new ProfessionalDetailsDTO();

		try {

			logger.info("Fetching ProfessionalDetails for candidate Id: " + candidateId);
			professionalDetailsDTO.setProfessionalDetails(professionalDetailsRepository.findByCandidateId(candidateId));
			logger.info("Fetching ProfessionalExperience ");
			professionalDetailsDTO
					.setProfessionalExperiences(professionalExperienceRepository.findByCandidateId(candidateId));
			return ResponseDTO.success(professionalDetailsDTO);

		} catch (Exception e) {
			logger.error("Error while fetching professional details for candidate Id " + candidateId, e);
			return ResponseDTO.error(ErrorMessagesConstants.ERROR_FETCHING_PROFESSIONAL_DETAILS);
		}

	}

	@Override
	public ResponseDTO saveEducationalDetails(EducationalDetailsCollection educationalDetails) {
		logger.info("Inside saveEducationalDetails()");

		try {

			logger.info("Saving the Educational Details for candidate Id :" + educationalDetails.getCandidateId());

			educationalDetailsRepository.save(educationalDetails);

			return ResponseDTO.success(MessageConstants.EDUCATIONAL_DETAILS_SAVED);

		} catch (Exception e) {
			logger.error(
					"Error while saving educational details for candidate Id " + educationalDetails.getCandidateId(),
					e);
			return ResponseDTO.error(ErrorMessagesConstants.ERROR_SAVING_EDUCATIONAL_DETAILS);
		}
	}

	@Override
	public ResponseDTO getEducationalDetails(Long candidateId) {
		logger.info("Inside getEducationalDetails()");

		try {
			logger.info("Fetching EducationalDetails for candidate Id: " + candidateId);
			return ResponseDTO.success(educationalDetailsRepository.findByCandidateId(candidateId));

		} catch (Exception e) {
			logger.error("Error while fetching educational details for candidate Id " + candidateId, e);
			return ResponseDTO.error(ErrorMessagesConstants.ERROR_FETCHING_EDUCATIONAL_DETAILS);
		}

	}

	@Override
	public ResponseDTO saveSocialDetails(SocialDetailsCollection socialDetails) {
		logger.info("Inside saveSocialDetails()");

		try {
			logger.info("Saving the Social Details for candidate Id :" + socialDetails.getCandidateId());

			socialDetailsRepository.save(socialDetails);

			return ResponseDTO.success(MessageConstants.SOCIAL_DETAILS_SAVED);

		} catch (Exception e) {
			logger.error("Error while saving social details for candidate Id " + socialDetails.getCandidateId(), e);
			return ResponseDTO.error(ErrorMessagesConstants.ERROR_SAVING_SOCIAL_DETAILS);

		}

	}

	@Override
	public ResponseDTO getSocialDetails(Long candidateId) {
		logger.info("Inside getSocialDetails()");

		try {
			logger.info("Fetching SocialDetails for candidate Id: " + candidateId);
			return ResponseDTO.success(socialDetailsRepository.findByCandidateId(candidateId));

		} catch (Exception e) {
			logger.error("Error while fetching social details for candidate Id " + candidateId, e);
			return ResponseDTO.error(ErrorMessagesConstants.ERROR_FETCHING_SOCIAL_DETAILS);
		}
	}

}
