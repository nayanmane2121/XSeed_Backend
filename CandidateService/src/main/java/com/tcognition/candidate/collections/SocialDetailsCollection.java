package com.tcognition.candidate.collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Social Details")
public class SocialDetailsCollection {

	@Id
	private Long id;
	private Long candidateId;
	private String linkedIn;
	private String twitter;
	private String website;
	private String other;
	private boolean developer;
	private boolean designer;
	private String github;
	private String leetcode;
	private String hackerRank;
	private String otherProfessionalProfile;
	private String portfolio;
	private String behance;
	private String dribble;
}
