package com.tcognition.candidate.collections;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Professional Details")
public class ProfessionalDetailsCollection {

	@Id
	private Long id;
	private Long candidateId;
	private String currentCompensation;
	private String expectedCompensation;
	private String preferredJobLocation;
	private String[] skills;
	
	
	
	
}
