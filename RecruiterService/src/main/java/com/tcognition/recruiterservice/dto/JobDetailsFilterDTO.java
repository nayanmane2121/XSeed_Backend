package com.tcognition.recruiterservice.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class JobDetailsFilterDTO {

	private List<String> priority;
	private List<String> jobTitle;
	private List<String> jobType;
	private List<String> expYears;
	private List<String> companyName;
	private List<String> vendorPlatform;
	private List<String> engagementType;
	private List<String> clientName;
	private int noOfOpenings;
	private int maxSubmission;
	private List<String> payRate;
	private List<String> workAuthorization;
	private int requiredHours;
	private List<String> requiredDocuments;
	private Date jobOpenDate;
	private Date jobCloseDate;
	private Date contractOpenDate;
	private Date contractCloseDate;
	private Double placementFeePercentage;
	private String turnaroundTime;
	private List<String> country;
	private List<String> state;
	private List<String> city;
	private List<String> status;

}
