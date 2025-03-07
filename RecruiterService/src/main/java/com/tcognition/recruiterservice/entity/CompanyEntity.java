package com.tcognition.recruiterservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "company_details")
public class CompanyEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String companyName;
	private String contact;
	private String email;
	private String linkedin;
	private String website;
	private String country;
	private String state;
	private String city;
	private String clientCategorization;
}
