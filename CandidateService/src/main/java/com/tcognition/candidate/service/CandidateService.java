package com.tcognition.candidate.service;

import com.tcognition.candidate.collections.CandidateCollection;
import com.tcognition.candidate.collections.EducationalDetailsCollection;
import com.tcognition.candidate.collections.SocialDetailsCollection;
import com.tcognition.candidate.dto.ProfessionalDetailsDTO;
import com.tcognition.candidate.dto.ResponseDTO;

public interface CandidateService {

	public ResponseDTO getAllCandidates();
	public ResponseDTO getCandidateDetails(Long id);
	public ResponseDTO registerCandidate(CandidateCollection candidate);
	public ResponseDTO saveProfessionalDetails(ProfessionalDetailsDTO professionalDetailsDTO);
	public ResponseDTO getProfessionalDetails(Long candidateId);
	public ResponseDTO saveEducationalDetails(EducationalDetailsCollection educationalDetails);
	public ResponseDTO getEducationalDetails(Long candidateId);
	public ResponseDTO saveSocialDetails(SocialDetailsCollection socialDetails);
	public ResponseDTO getSocialDetails(Long candidateId);
	
}
