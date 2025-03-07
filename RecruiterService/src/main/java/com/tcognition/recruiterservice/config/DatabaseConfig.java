package com.tcognition.recruiterservice.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DatabaseConfig {
	private final Environment env;

	public DatabaseConfig(Environment env) {
		this.env = env;
	}

	@Bean
	public Connection databaseConnection() throws SQLException {
		return DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));
	}
}