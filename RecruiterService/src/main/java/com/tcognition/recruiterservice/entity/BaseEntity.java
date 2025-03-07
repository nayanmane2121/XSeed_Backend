package com.tcognition.recruiterservice.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

	@CreatedDate
	@Column(name = "Created_On")
	private Date createdOn;

	@CreatedBy
	@Column(name = "Created_By")
	private String createdBy;

	@LastModifiedDate
	@Column(name = "Updated_On")
	private Date updatedOn;

	@LastModifiedBy
	@Column(name = "Updated_By")
	private String updatedBy;
}
