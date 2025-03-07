//package com.tcognition.emailservice.utils;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//
//import java.security.Key;
//import io.jsonwebtoken.security.Keys;
//
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JwtUtil {
//
//	private final String secret;
//
//	public JwtUtil(Environment environment) {
//		this.secret = environment.getProperty("security.jwt.secret");
//	}
//
//	private Key getSigningKey() {
//		return Keys.hmacShaKeyFor(secret.getBytes());
//	}
//
//	public Claims extractClaims(String token) {
//		try {
//			return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
//		} catch (Exception ex) {
//			throw new IllegalArgumentException("Invalid token");
//		}
//	}
//
//	public boolean validateToken(String token) {
//		try {
//			extractClaims(token);
//			return true;
//		} catch (Exception ex) {
//			return false;
//		}
//	}
//}
