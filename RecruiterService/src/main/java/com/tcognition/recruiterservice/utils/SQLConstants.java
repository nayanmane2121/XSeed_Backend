package com.tcognition.recruiterservice.utils;

public class SQLConstants {

	public static final String PROCEDURE_FETCH_INTERVIEW_DETAILS_BY_JOB = "SELECT * FROM fetch_interview_details_by_job(?)";
	public static final String PROCEDURE_SCHEDULE_INTERVIEW = "SELECT * FROM schedule_interview(?, ?, ?, ?, ?, ?, ?, ?, ?)";
}
