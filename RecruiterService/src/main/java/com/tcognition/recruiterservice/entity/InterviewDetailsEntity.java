package com.tcognition.recruiterservice.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "interview_details")
@Data
public class InterviewDetailsEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "job_details", referencedColumnName = "id")
	private JobDetailsEntity jobDetailsEntity;

	@Column(name = "interview_date_time")
	private LocalDateTime interviewDateTime;

	@Column(name = "platform")
	private String platform;

	@Column(name = "candidate_name")
	private String candidateName;

	@Column(name = "candidate_email")
	private String candidateEmail;

	@OneToMany(mappedBy = "interviewDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PanelEntity> panelDetails = new ArrayList<>();

}
