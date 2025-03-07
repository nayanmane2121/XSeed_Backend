package com.tcognition.recruiterservice.dto;

import com.tcognition.recruiterservice.utils.AppConstants;

import lombok.Data;

@Data
public class ResponseDTO<T> {

	private String status; // e.g., "SUCCESS", "ERROR"
	private String message; // Description of the response
	private T data;

	public ResponseDTO(String status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public static <T> ResponseDTO<T> success(T data) {
		return new ResponseDTO<>(AppConstants.STATUS_SUCCESS, AppConstants.STATUS_SUCCESS_MESSAGE, data);
	}

	public static <T> ResponseDTO<T> error(String message) {
		return new ResponseDTO<>(AppConstants.STATUS_ERROR, message, null);
	}

	@Override
	public String toString() {
		return "ResponseDTO{" + "status='" + status + '\'' + ", message='" + message + '\'' + ", data=" + data + '}';
	}
}
