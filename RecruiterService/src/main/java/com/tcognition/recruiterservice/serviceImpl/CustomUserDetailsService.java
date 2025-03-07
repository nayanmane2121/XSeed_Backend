package com.tcognition.recruiterservice.serviceImpl;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Since Recruiter Service has no user DB, return a dummy UserDetails object
		return User.withUsername(username).password("") // Password is not needed for JWT authentication
				.authorities(Collections.emptyList()) // Add roles if needed
				.build();
	}
}