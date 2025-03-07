package com.tcognition.recruiterservice.serviceImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.tcognition.recruiterservice.dto.EmailAttachmentRequestDTO;
import com.tcognition.recruiterservice.dto.EmailRequestDTO;
import com.tcognition.recruiterservice.dto.InterviewDetailsDTO;
import com.tcognition.recruiterservice.entity.PanelEntity;
import com.tcognition.recruiterservice.service.EmailServiceClient;
import com.tcognition.recruiterservice.service.MeetingInviteSender;
import com.tcognition.recruiterservice.utils.AppConstants;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;

import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class GoogleMeetInviteSender implements MeetingInviteSender {

	private static final String APPLICATION_NAME = "XseedAi Meet Scheduler";

	@SuppressWarnings("deprecation")
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	// private static final String TOKENS_DIRECTORY_PATH = "tokens";

	private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

	@Override
	public EmailRequestDTO scheduleInterview(InterviewDetailsDTO interviewDetailsDTO) {
		EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
		try {
			// Get panelist emails
			List<String> panelEmails = interviewDetailsDTO.getPanel().stream().map(PanelEntity::getPanelEmail)
					.collect(Collectors.toList());

			// Add candidate email
			panelEmails.add(interviewDetailsDTO.getCandidateEmail());

			// Schedule Google Meet with panelists and candidate
			String meetingLink = scheduleMeeting("Interview - " + interviewDetailsDTO.getJobTitle(),
					interviewDetailsDTO.getInterviewDateTime().toString(), panelEmails);

			// Prepare email content
			emailRequestDTO.setTo(interviewDetailsDTO.getCandidateEmail());
			emailRequestDTO.setName(interviewDetailsDTO.getCandidateName());
			emailRequestDTO.setSubject("Interview - " + interviewDetailsDTO.getJobTitle());

			if (!panelEmails.isEmpty()) {
				emailRequestDTO.setCc(panelEmails.toArray(new String[0])); // Convert to String[]
			}

			Map<String, String> data = new HashMap<>();
			data.put("meetingLink", meetingLink);
			data.put("dateTime", interviewDetailsDTO.getInterviewDateTime().toString());
			data.put("role", interviewDetailsDTO.getJobTitle());

			emailRequestDTO.setData(data);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		return emailRequestDTO;
	}

	/**
	 * Load OAuth 2.0 credentials.
	 */
	private static Credential getCredentials(HttpTransport httpTransport) throws IOException {
		InputStream in = new FileInputStream(AppConstants.CALENDAR_KEY_PATH);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
				clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(AppConstants.CALENDAR_TOKEN)))
				.setAccessType("offline").build();

		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(AppConstants.USER);
	}

	public static String scheduleMeeting(String summary, String startTime, List<String> attendeesEmails)
	        throws IOException, GeneralSecurityException {
	    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	    Credential credential = getCredentials(httpTransport);

	    Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
	            .setApplicationName(APPLICATION_NAME).build();

	    // Convert start time to ZonedDateTime in IST
	    LocalDateTime startDateTime = LocalDateTime.parse(startTime);
	    ZonedDateTime zonedStartDateTime = startDateTime.atZone(ZoneId.of(AppConstants.TIMEZONE));
	    ZonedDateTime zonedEndDateTime = zonedStartDateTime.plusMinutes(30);

	    // Format for Google Calendar API
	    DateTime startDateTimeGoogle = new DateTime(zonedStartDateTime.toInstant().toString());
	    DateTime endDateTimeGoogle = new DateTime(zonedEndDateTime.toInstant().toString());

	    // Create event
	    Event event = new Event()
	            .setSummary(summary)
	            .setStart(new EventDateTime().setDateTime(startDateTimeGoogle).setTimeZone(AppConstants.TIMEZONE))
	            .setEnd(new EventDateTime().setDateTime(endDateTimeGoogle).setTimeZone(AppConstants.TIMEZONE))
	            .setConferenceData(new ConferenceData().setCreateRequest(
	                    new CreateConferenceRequest().setRequestId("meet-" + System.currentTimeMillis())
	                            .setConferenceSolutionKey(new ConferenceSolutionKey().setType("hangoutsMeet"))
	            ));

	    // Add attendees (Panel + Candidate)
	    List<EventAttendee> attendees = attendeesEmails.stream()
	            .map(email -> new EventAttendee().setEmail(email))
	            .collect(Collectors.toList());

	    event.setAttendees(attendees);

	    // Restrict access to only invited attendees
	    event.setGuestsCanModify(false);
	    event.setGuestsCanInviteOthers(false);
	    event.setGuestsCanSeeOtherGuests(true);

	    // Insert event into Google Calendar
	    event = service.events().insert("primary", event)
	            .setConferenceDataVersion(1)
	            .setSendUpdates("all") // Send invites to all attendees
	            .execute();

	    // Get Google Meet link
	    String meetLink = event.getHangoutLink();
	    
	    // 🔹 Prevent unauthorized users from joining (WORKS ONLY FOR GOOGLE WORKSPACE ACCOUNTS)
	    event.setLocked(true);  // Prevent non-invitees from joining (Requires Workspace)
	    service.events().patch("primary", event.getId(), event).execute();

	    System.out.println("Google Meet Link: " + meetLink);

	    return meetLink;
	}

}
