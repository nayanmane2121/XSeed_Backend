package com.tcognition.recruiterservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tcognition.recruiterservice.entity.PanelEntity;

import lombok.Data;

@Data
public class InterviewDetailsDTO {

	private Long interviewId;
	private Long jobId;
	private String jobTitle;
	private String JDFilePath;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime interviewDateTime;
	private String platform;
	private String jobDescription;
	private String candidateName;
	private String candidateEmail;
	private List<PanelEntity> panel;	
	
}
