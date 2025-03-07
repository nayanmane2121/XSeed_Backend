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
@Table(name = "client_details")
public class CompanyClientEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String clientName;
	private String engagementType;
	private String agreementType;
	private Double margin;
	private String vendorPlatform;
	private String contactName;
	private String contactEmail;

	@ManyToOne
	@JoinColumn(name = "company", referencedColumnName = "id")
	private CompanyEntity company;

}
