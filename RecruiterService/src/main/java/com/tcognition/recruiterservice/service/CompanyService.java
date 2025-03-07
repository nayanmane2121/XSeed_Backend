package com.tcognition.recruiterservice.service;

import com.tcognition.recruiterservice.dto.ResponseDTO;

public interface CompanyService {

	public ResponseDTO getCompanyDetails(Long clientId);
	public ResponseDTO getAllCompanies();
}
