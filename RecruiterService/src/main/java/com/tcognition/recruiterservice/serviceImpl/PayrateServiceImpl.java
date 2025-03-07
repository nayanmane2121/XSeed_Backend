package com.tcognition.recruiterservice.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcognition.recruiterservice.controller.ClientController;
import com.tcognition.recruiterservice.dto.ResponseDTO;
import com.tcognition.recruiterservice.entity.CompanyEntity;
import com.tcognition.recruiterservice.entity.PayRateEntity;
import com.tcognition.recruiterservice.repository.CompanyRepository;
import com.tcognition.recruiterservice.repository.PayrateRepository;
import com.tcognition.recruiterservice.service.PayrateService;

@Service
public class PayrateServiceImpl implements PayrateService {

	@Autowired
	PayrateRepository payrateRepository;

	@Autowired
	CompanyRepository companyRepository;

	private static final Logger logger = LoggerFactory.getLogger(PayrateServiceImpl.class);

	@Override
	public ResponseDTO getPayRateDetails(Long companyId) {

		try {

			CompanyEntity company = companyRepository.findById(companyId).get();

			if (company != null) {

				List<PayRateEntity> payRates = payrateRepository.findByCompany(company);

				if (payRates != null) {

					return ResponseDTO.success(payRates);

				} else {

					return ResponseDTO.error("No pay rates found for company ID: " + companyId);

				}

			} else {
				return ResponseDTO.error("No company found with ID: " + companyId);
			}

		} catch (Exception e) {

			logger.error("Error while fetching the pay rates" + e.getMessage());
			return ResponseDTO.error("Error while fetching the pay rates for company ID:  " + companyId);
		}

	}

}
