//package com.tcognition.userservice.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Service
//public class RestTemplateService {
//
//	@Autowired
//	private RedisTemplate<String, String> redisTemplate;
//	
//	private final RestTemplate restTemplate;
//	
//	
//
//	// Constructor injection of RestTemplate
//	public RestTemplateService(RestTemplate restTemplate, ObjectMapper objectMapper) {
//	        this.restTemplate = restTemplate;
//	    }
//
//	public String callExternalService(String url) {
//		try {
//			// Making a GET request
//			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//
//			// Return the response body as a String
//			return response.getBody();
//		} catch (HttpClientErrorException e) {
//			// Handle any client errors
//			return "Error calling service: " + e.getMessage();
//		} catch (Exception e) {
//			// Handle general errors
//			return "An error occurred: " + e.getMessage();
//		}
//	}
//	
//    // Generic method to handle any DTO
//    public <T> String callExternalServiceWithBody(String url, T requestBody) throws Exception {
//        // Set headers to indicate JSON content type
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue(); 
//
//        headers.set("Authorization", "Bearer " + valueOperations.get("Token"));
//        
//        // Wrap the request body and headers in HttpEntity
//        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, headers);
//
//        // Send POST request
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//
//        return response.getBody();
//    }
//}