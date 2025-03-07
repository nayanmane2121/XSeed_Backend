package com.tcognition.candidate.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.tcognition.candidate.collections.CandidateCollection;

public interface CandidateRepository extends MongoRepository<CandidateCollection, Long>{

	CandidateCollection findByEmail(String email); 
	Optional<CandidateCollection> findById(Long id); 
}
