package com.tcognition.recruiterservice.Factory;

import com.tcognition.recruiterservice.service.MeetingInviteSender;
import com.tcognition.recruiterservice.serviceImpl.GoogleMeetInviteSender;
import com.tcognition.recruiterservice.serviceImpl.TeamsInviteSender;
import com.tcognition.recruiterservice.serviceImpl.ZoomInviteSender;
import com.tcognition.recruiterservice.utils.AppConstants;

public class MeetingInviteFactory {

	public static MeetingInviteSender getInterviewScheduler(String platform) {
        return switch (platform.toLowerCase()) {
            case AppConstants.PLATFORM_GOOGLE_MEET -> new GoogleMeetInviteSender();
            case AppConstants.PLATFORM_ZOOM -> new ZoomInviteSender();
            case AppConstants.PLATFORM_TEAMS -> new TeamsInviteSender();
            default -> throw new IllegalArgumentException("Unsupported platform: " + platform);
        };
    }
}
