package com.tcognition.candidate.collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Counters")
public class Counter {
    
    @Id
    private String id;
    private Long sequence;
}
