package com.tcognition.candidate.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tcognition.candidate.collections.ProfessionalDetailsCollection;
import java.util.List;


public interface ProfessionalDetailsRepository extends MongoRepository<ProfessionalDetailsCollection, Long>{

	ProfessionalDetailsCollection findByCandidateId(Long candidateId);
}
