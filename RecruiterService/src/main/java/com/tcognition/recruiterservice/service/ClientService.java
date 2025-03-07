package com.tcognition.recruiterservice.service;

import com.tcognition.recruiterservice.dto.ResponseDTO;

public interface ClientService {

	public ResponseDTO getClientDetails(Long clientId);
	public ResponseDTO getAllClients(Long companyId);
	
}
