package com.tcognition.recruiterservice.serviceImpl;

import org.springframework.web.multipart.MultipartFile;

import com.tcognition.recruiterservice.dto.EmailAttachmentRequestDTO;
import com.tcognition.recruiterservice.dto.EmailRequestDTO;
import com.tcognition.recruiterservice.dto.InterviewDetailsDTO;
import com.tcognition.recruiterservice.service.MeetingInviteSender;

public class TeamsInviteSender implements MeetingInviteSender {

	@Override
	public EmailRequestDTO scheduleInterview(InterviewDetailsDTO interviewDetailsDTO) {return null;}
}
