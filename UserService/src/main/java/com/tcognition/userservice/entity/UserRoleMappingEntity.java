package com.tcognition.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_role_mapping")
@Data
public class UserRoleMappingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne 
    @JoinColumn(name = "user_id", referencedColumnName = "id")
	private UserEntity user;
	
    @ManyToOne 
    @JoinColumn(name = "role_id", referencedColumnName = "id")
	private RoleEntity role;
}
