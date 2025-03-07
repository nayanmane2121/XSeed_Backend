package com.tcognition.candidate.collections;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Educational Details")
public class EducationalDetailsCollection {

	@Id
	private Long id;
	private Long candidateId;
	private String schoolName;
	private String schoolType;
	private String degreeName;
	private String degreeType;
	private boolean pursuing;
	private Date startDate;
	private Date endDate;
	private String country;
	private String state;
	private String city;
	
}
