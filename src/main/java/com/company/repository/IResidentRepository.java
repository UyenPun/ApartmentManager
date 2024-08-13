package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.company.entity.Resident;

public interface IResidentRepository extends JpaRepository<Resident, Integer>, JpaSpecificationExecutor<Resident> {
	Boolean existsByEmail(String email);

	Boolean existsByIdCard(String IdCard);
}
