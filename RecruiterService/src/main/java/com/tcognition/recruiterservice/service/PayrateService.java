package com.tcognition.recruiterservice.service;

import com.tcognition.recruiterservice.dto.ResponseDTO;

public interface PayrateService {

	public ResponseDTO getPayRateDetails(Long clientId);
}
