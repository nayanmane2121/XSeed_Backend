package com.tcognition.recruiterservice.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcognition.recruiterservice.dto.JobDetailsDTO;
import com.tcognition.recruiterservice.dto.JobDetailsFilterDTO;
import com.tcognition.recruiterservice.dto.JobDetailsRequestDTO;
import com.tcognition.recruiterservice.dto.ResponseDTO;
import com.tcognition.recruiterservice.entity.JobDetailsEntity;
import com.tcognition.recruiterservice.repository.ClientRepository;
import com.tcognition.recruiterservice.repository.CompanyRepository;
import com.tcognition.recruiterservice.repository.JobDetailsRepository;
import com.tcognition.recruiterservice.repository.PayrateRepository;
import com.tcognition.recruiterservice.service.JobService;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class JobServiceImpl implements JobService {

	@Autowired
	JobDetailsRepository jobDetailsRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	PayrateRepository payrateRepository;

	@Override
	public ResponseDTO getJobDetails(JobDetailsRequestDTO jobDetailsRequestDTO) {

		return null;
	}

	@Override
	public ResponseDTO saveJobDetails(JobDetailsDTO jobDetailsDTO) {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JobDetailsEntity jobDetailsEntity = objectMapper.convertValue(jobDetailsDTO, JobDetailsEntity.class);

		jobDetailsEntity.setCompany(companyRepository.findById(jobDetailsDTO.getCompanyId()).get());
		jobDetailsEntity.setClient(clientRepository.findById(jobDetailsDTO.getClientId()).get());
		jobDetailsEntity.setPayRate(payrateRepository.findById(jobDetailsDTO.getPayRateId()).get());

		jobDetailsRepository.save(jobDetailsEntity);

		return ResponseDTO.success("Job Created successfully.");
	}

	public ResponseDTO getFilteredJobDetails(JobDetailsFilterDTO jobDetailsFilterDTO) {
		Specification<JobDetailsEntity> spec = filterJobs(jobDetailsFilterDTO);

		List<JobDetailsEntity> jobDetailsEntity = jobDetailsRepository.findAll(spec);

		return ResponseDTO.success(jobDetailsEntity);
	}

	private static Specification<JobDetailsEntity> filterJobs(JobDetailsFilterDTO jobDetailsFilterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Field[] fields = JobDetailsFilterDTO.class.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(jobDetailsFilterDTO);

                    if (isValidValue(value)) {
                        if (value instanceof List<?> list && !list.isEmpty()) {
                            // ✅ Apply IN condition for list-based filters
                            predicates.add(root.get(field.getName()).in(list));
                        } else {
                            // ✅ Apply EQUAL condition for single-value filters
                            predicates.add(criteriaBuilder.equal(root.get(field.getName()), value));
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing filter fields", e);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean isValidValue(Object value) {
        if (value == null) return false;
        if (value instanceof String && ((String) value).trim().isEmpty()) return false;
        if (value instanceof Integer && (Integer) value == 0) return false;
        if (value instanceof Double && (Double) value == 0.0) return false;
        if (value instanceof Boolean && !((Boolean) value)) return false; // Ignore false, filter only for true
        if (value instanceof List<?> list && list.isEmpty()) return false;
        return true;
    }
}
