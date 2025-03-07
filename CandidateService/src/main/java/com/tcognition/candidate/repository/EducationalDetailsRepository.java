package com.tcognition.candidate.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tcognition.candidate.collections.EducationalDetailsCollection;

public interface EducationalDetailsRepository extends MongoRepository<EducationalDetailsCollection, Long>{

	List<EducationalDetailsCollection> findByCandidateId(Long candidateId);
}
