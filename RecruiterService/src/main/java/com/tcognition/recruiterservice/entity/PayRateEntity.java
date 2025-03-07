package com.tcognition.recruiterservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "payrate_details")
public class PayRateEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String jobTitle;
	private String experience;
	private String rate;
	private String unit;

	@ManyToOne
	@JoinColumn(name = "company", referencedColumnName = "id")
	private CompanyEntity company;

}
