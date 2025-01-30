package com.tcognition.userservice.jwt;

import io.jsonwebtoken.Jwts;
import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Duration;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.tcognition.userservice.dto.JwtClaimsDTO;
import com.tcognition.userservice.utils.ErrorMessagesConstants;

@Component
public class JwtService {

	@Autowired
    private RedisTemplate<String, String> redisTemplate;
	
	private final String secret;
	private final Long expiration;

	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

	public JwtService(Environment environment) {
		this.secret = environment.getProperty("security.jwt.secret");
		this.expiration = Long.parseLong(environment.getProperty("security.jwt.expiration", "300000")); // Default to 5 minutes
	}

	public String generateToken(JwtClaimsDTO jwtClaimsDTO) {

		logger.info("Inside generateToken()");
		logger.info("Generating token...");

		String token = null;

		try {
			token = Jwts.builder().setSubject(jwtClaimsDTO.getUserName()).claim("role", jwtClaimsDTO.getUserRole())
					.claim("userId", jwtClaimsDTO.getUserId()).setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + expiration))
					.signWith(new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName())).compact();
		} catch (Exception e) {
			logger.error(ErrorMessagesConstants.ERROR_TOKEN_GENERATION, e);
		}
		
		logger.info("Token generated...");
		// Save token to Redis with expiration
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("Token:"+jwtClaimsDTO.getUserName() , token, Duration.ofMillis(expiration));

		return token;
	}
}