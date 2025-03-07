package com.tcognition.recruiterservice.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "job_details")
@Data
public class JobDetailsEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	private String priority;
	private Long requisitionId;
	private String jobTitle;
	private String jobType;
	private String expYears;

	@ManyToOne
	@JoinColumn(name = "company", referencedColumnName = "id")
	private CompanyEntity company;

	private String vendorPlatform;
	private String engagementType;

	@ManyToOne
	@JoinColumn(name = "client", referencedColumnName = "id")
	private CompanyClientEntity client;

	private int noOfOpenings;
	private int maxSubmission;

	@ManyToOne
	@JoinColumn(name = "pay_rate", referencedColumnName = "id")
	private PayRateEntity payRate;

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

	@Lob
	@Column(name = "job_description", columnDefinition = "TEXT")
	private String jobDescription;

}
