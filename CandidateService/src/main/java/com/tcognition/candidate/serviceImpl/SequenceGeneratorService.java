package com.tcognition.candidate.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tcognition.candidate.collections.Counter;

@Service
public class SequenceGeneratorService {

	@Autowired
	private MongoOperations mongoOperations;

	public Long generateSequence(String seqName) {
		Query query = new Query(Criteria.where("_id").is(seqName));
		Update update = new Update().inc("sequence", 1);
		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);

		Counter counter = mongoOperations.findAndModify(query, update, options, Counter.class);
		return (counter != null) ? counter.getSequence() : 1L;
	}

}
