package com.tcognition.candidate.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.tcognition.candidate.collections.ProfessionalExperienceCollection;
import java.util.List;


public interface ProfessionalExperienceRepository extends MongoRepository<ProfessionalExperienceCollection, Long>{

  List<ProfessionalExperienceCollection> findByCandidateId(Long candidateId);
}
