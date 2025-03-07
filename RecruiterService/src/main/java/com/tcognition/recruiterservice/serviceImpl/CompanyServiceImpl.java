package com.tcognition.recruiterservice.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcognition.recruiterservice.dto.ResponseDTO;
import com.tcognition.recruiterservice.entity.CompanyEntity;
import com.tcognition.recruiterservice.repository.CompanyRepository;
import com.tcognition.recruiterservice.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

	private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Autowired
	CompanyRepository companyRepository;

	@Override
	public ResponseDTO getCompanyDetails(Long clientId) {

		try {
			CompanyEntity companyEntity = companyRepository.findById(clientId).get();
			if (companyEntity != null) {

				return ResponseDTO.success(companyEntity);
			} else {
				return ResponseDTO.error("No Companies found..");
			}

		} catch (Exception e) {
			logger.error("Error while fetching company list: " + e.getMessage());
			return ResponseDTO.error("Error while fetching company list");
		}

	}

	@Override
	public ResponseDTO getAllCompanies() {

		try {
			List<CompanyEntity> companies = companyRepository.findAll();

			if (companies != null) {

				return ResponseDTO.success(companies);
			} else {
				return ResponseDTO.error("No Companies found..");
			}

		} catch (Exception e) {
			logger.error("Error while fetching company list: " + e.getMessage());
			return ResponseDTO.error("Error while fetching company list");
		}

	}

}
