package com.tcognition.recruiterservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.tcognition.recruiterservice.jwt.JwtAuthFilter;


@Configuration
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;

	public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
//				.requestMatchers(new AntPathRequestMatcher("/api/auth/**", HttpMethod.OPTIONS.name())).permitAll()
//				.requestMatchers(new AntPathRequestMatcher("/api/auth/**", HttpMethod.POST.name())).permitAll()
//				.requestMatchers(new AntPathRequestMatcher("/api/admin/createsuperadmin", HttpMethod.POST.name())).permitAll()
				.anyRequest().authenticated() // Secure all other requests
		).csrf(csrf -> csrf.disable()) // Disable CSRF for APIs (since using JWT)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

}
