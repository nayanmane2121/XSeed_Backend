package com.tcognition.candidate.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tcognition.candidate.collections.SocialDetailsCollection;

public interface SocialDetailsRepository extends MongoRepository<SocialDetailsCollection, Long> {

	List<SocialDetailsCollection> findByCandidateId(Long candidateId);

}
