package com.tcognition.recruiterservice.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "panel_details")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PanelEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "job_details", referencedColumnName = "id")
	private JobDetailsEntity jobDetailsEntity;

	@ManyToOne
	@JoinColumn(name = "interview_details_id", referencedColumnName = "id")
	private InterviewDetailsEntity interviewDetails;

	@Column(name = "panel_name")
	private String panelName;

	@Column(name = "panel_email")
	private String panelEmail;

}
