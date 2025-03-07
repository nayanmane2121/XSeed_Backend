package com.tcognition.recruiterservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private final UserDetailsService userDetailsService;

	@Value("${security.jwt.expiration}")
	private String expiration;

	@Value("${security.jwt.secret}")
	private String secret;

	public JwtAuthFilter(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String token = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
			try {
				Claims claims = Jwts.parserBuilder().setSigningKey(new SecretKeySpec(secret.getBytes(), "HmacSHA256"))
						.build().parseClaimsJws(token).getBody();

				username = claims.getSubject();

				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);

					if (validateToken(token, userDetails)) {
						UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					}
				}
			} catch (io.jsonwebtoken.ExpiredJwtException e) {
				// Handle expired token logic
				handleExpiredToken(e, response, token, request);
			} catch (Exception e) {
				logger.error("Invalid JWT token: " + e.getMessage());
				sendUnauthorizedResponse(response, "Invalid JWT token");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	private void handleExpiredToken(io.jsonwebtoken.ExpiredJwtException e, HttpServletResponse response, String token,
			HttpServletRequest request) throws IOException {
		String username = e.getClaims().getSubject();

		if (isTokenInRedis(token, username)) {
			// Refresh the token expiration in Redis
			redisTemplate.expire("Token:" + username, Duration.ofMillis(Long.parseLong(expiration)));

			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		} else {
			sendUnauthorizedResponse(response, "Session expired. Please log in again.");
		}
	}

	private boolean validateToken(String token, UserDetails userDetails) {
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(new SecretKeySpec(secret.getBytes(), "HmacSHA256"))
					.build().parseClaimsJws(token).getBody();

			String username = claims.getSubject();
			return username.equals(userDetails.getUsername()) && (!isTokenExpired(claims) || isTokenInRedis(token, username));
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isTokenExpired(Claims claims) {
		return claims.getExpiration().before(new Date());
	}

	private boolean isTokenInRedis(String token, String username) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		String storedToken = valueOperations.get("Token:" + username);
		return storedToken != null && storedToken.equals(token);
	}

	private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write("{\"message\": \"" + message + "\"}");
		response.getWriter().flush();
	}
}
