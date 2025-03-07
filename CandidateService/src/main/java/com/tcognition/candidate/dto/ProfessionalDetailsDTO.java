package com.tcognition.candidate.dto;

import java.util.List;

import com.tcognition.candidate.collections.ProfessionalDetailsCollection;
import com.tcognition.candidate.collections.ProfessionalExperienceCollection;

import lombok.Data;

@Data
public class ProfessionalDetailsDTO {

	private ProfessionalDetailsCollection professionalDetails;
	private List<ProfessionalExperienceCollection> professionalExperiences;
}
