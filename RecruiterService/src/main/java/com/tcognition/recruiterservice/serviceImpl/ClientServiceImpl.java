package com.tcognition.recruiterservice.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcognition.recruiterservice.dto.ResponseDTO;
import com.tcognition.recruiterservice.entity.CompanyClientEntity;
import com.tcognition.recruiterservice.entity.CompanyEntity;
import com.tcognition.recruiterservice.repository.ClientRepository;
import com.tcognition.recruiterservice.repository.CompanyRepository;
import com.tcognition.recruiterservice.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	CompanyRepository companyRepository;

	private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

	@Override
	public ResponseDTO getClientDetails(Long clientId) {

		try {
			CompanyClientEntity client = clientRepository.findById(clientId).get();

			if (client != null) {

				return ResponseDTO.success(client);

			} else {
				return ResponseDTO.error("No client found for Id: " + clientId);
			}

		} catch (Exception e) {
			logger.error("Error while fetching the client" + e.getMessage());
			return ResponseDTO.error("Error while fetching the client ID:  " + clientId);
		}
	}

	@Override
	public ResponseDTO getAllClients(Long companyId) {

		try {

			CompanyEntity company = companyRepository.findById(companyId).get();

			if (company != null) {

				List<CompanyClientEntity> clients = clientRepository.findByCompany(company);

				if (clients != null) {

					return ResponseDTO.success(clients);

				} else {

					return ResponseDTO.error("No clients found for company ID: " + companyId);

				}

			} else {
				return ResponseDTO.error("No company found with ID: " + companyId);
			}

		} catch (Exception e) {

			logger.error("Error while fetching the clients" + e.getMessage());
			return ResponseDTO.error("Error while fetching the clients for company ID:  " + companyId);
		}
	}

}
