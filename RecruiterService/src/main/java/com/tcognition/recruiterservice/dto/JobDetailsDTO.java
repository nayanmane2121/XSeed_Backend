package com.tcognition.recruiterservice.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobDetailsDTO {

	private Long id;
	private String priority;
	private Long requisitionId;
	private String jobTitle;
	private String jobType;
	private String expYears;
	private Long companyId;
	private String vendorPlatform;
	private String engagementType;
	private Long clientId;
	private int noOfOpenings;
	private int maxSubmission;
	private Long payRateId;
	private String workAuthorization;
	private int requiredHours;
	private String requiredDocuments;
	private Date jobOpenDate;
	private Date jobCloseDate;
	private Date contractOpenDate;
	private Date contractCloseDate;
	private Double placementFeePercentage;
	private String turnaroundTime;
	private String country;
	private String state;
	private String city;
	private String status;
	private String jobDetailsFilePath;
	private boolean isDeleted;
	private String jobDescription;

}
