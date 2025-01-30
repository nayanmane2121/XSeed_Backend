package com.tcognition.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignConfig {

//	private final StringRedisTemplate redisTemplate;
//
//	public FeignConfig(StringRedisTemplate redisTemplate) {
//		this.redisTemplate = redisTemplate;
//	}
//
//	@Bean
//	public RequestInterceptor requestInterceptor() {
//
//		return new RequestInterceptor() {
//
//			@Override
//			public void apply(RequestTemplate template) {
//
//				String token = getTokenFromRedis();
//				if (token != null) {
//					template.header("Authorization", "Bearer " + token);
//				}
//			}
//
//			private String getTokenFromRedis() {
//
//				String username = getCurrentUsername();
//				return redisTemplate.opsForValue().get("Token:" + username);
//			}
//
//			private String getCurrentUsername() {
//				return SecurityContextHolder.getContext().getAuthentication().getName();
//			}
//		};
//	}
}
