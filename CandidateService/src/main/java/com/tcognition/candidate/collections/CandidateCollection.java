package com.tcognition.candidate.collections;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Candidate")
public class CandidateCollection {

	@Transient
    public static final String SEQUENCE_NAME = "candidate_sequence";
	
	@Id
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String contactNo;
	private String gender;
	private Date dob;
	private String address;
	private String resumePath;
	private String videoResumePath;
	private String profilePicturePath;
}
