//package com.tcognition.emailservice.security;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.tcognition.emailservice.utils.JwtUtil;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtAuthFilter extends OncePerRequestFilter {
//
//	@Autowired
//	private RedisTemplate<String, String> redisTemplate;
//
//	private final JwtUtil jwtUtil;
//
//	public JwtAuthFilter(JwtUtil jwtUtil) {
//		this.jwtUtil = jwtUtil;
//	}
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//			throws ServletException, IOException {
//
//		String username = null;
//		String token = null;
//
//		try {
//			final String authHeader = request.getHeader("Authorization");
//
//			if (authHeader != null && authHeader.startsWith("Bearer ")) {
//				token = authHeader.substring(7);
//
//				if (jwtUtil.validateToken(token)) {
//					Claims claims = jwtUtil.extractClaims(token);
//					username = claims.getSubject();
//
//					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//							username, null, null);
//					SecurityContextHolder.getContext().setAuthentication(authentication);
//				}
//
//			}
//
//		} catch (io.jsonwebtoken.ExpiredJwtException e) {
//			Claims claims = e.getClaims();
//			username = claims.getSubject();
//
//			ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//			String storedToken = valueOperations.get("Token"+ username);
//
//			// Check Redis for session continuation if token is expired
//
//			if (token.equals(storedToken)) {
//				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
//						null, null);
//				SecurityContextHolder.getContext().setAuthentication(authentication);
//
//			} else {
//				logger.error("Invalid JWT token: " + e.getMessage());
//				sendSessionExpiredResponse(response);
//				return;
//			}
//		}
//		chain.doFilter(request, response);
//	}
//
//	private void sendSessionExpiredResponse(HttpServletResponse response) throws IOException {
//		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
//		response.setContentType("application/json");
//		response.getWriter().write("{\"message\": \"Session expired. Please log in again.\"}");
//		response.getWriter().flush();
//	}
//}