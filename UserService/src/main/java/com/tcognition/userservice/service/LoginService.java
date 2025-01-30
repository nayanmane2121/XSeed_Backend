package com.tcognition.userservice.service;

import com.tcognition.userservice.dto.LoginRequestDTO;
import com.tcognition.userservice.dto.ResetPasswordRequestDTO;
import com.tcognition.userservice.dto.ResponseDTO;
import com.tcognition.userservice.dto.ValidateOtpDTO;

public interface LoginService {

	public ResponseDTO login(LoginRequestDTO loginRequest);
	public ResponseDTO resetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO);
	public ResponseDTO validateOTP(ValidateOtpDTO ValidateOtpDTO);
}
