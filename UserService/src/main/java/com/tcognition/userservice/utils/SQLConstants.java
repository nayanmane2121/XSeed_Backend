package com.tcognition.userservice.utils;

public class SQLConstants {

	public static final String QUERY_FIND_USER_BY_EMAIL_OR_CONTACT = "SELECT u FROM UserEntity u WHERE LOWER(u.email) = LOWER(:userContact) OR LOWER(u.contact) = LOWER(:userContact)";
}
