package com.tcognition.recruiterservice.service;

import com.tcognition.recruiterservice.dto.EmailRequestDTO;
import com.tcognition.recruiterservice.dto.InterviewDetailsDTO;

public interface MeetingInviteSender {

	EmailRequestDTO scheduleInterview(InterviewDetailsDTO interviewDetailsDTO);

}
