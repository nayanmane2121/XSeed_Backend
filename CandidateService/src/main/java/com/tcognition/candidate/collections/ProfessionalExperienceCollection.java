package com.tcognition.candidate.collections;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Document(collection = "Professional Experience")
public class ProfessionalExperienceCollection {

	@Id
	private Long id;
	private Long candidateId;
	private String companyName;
	private String jobLocation;
	private boolean remote;
	private boolean current;
	private Date startDate;
	private Date endDate;
	private String compensation;
	private String compensationType;
	private String employmentType;
	private String reasonForLeaving;
}
