package com.company.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.company.entity.Resident;
import com.company.entity.Resident.ResidentStatus;

public interface ResidentRepository extends JpaRepository<Resident, Integer>, JpaSpecificationExecutor<Resident> {
	Boolean existsByEmail(String email);

	Boolean existsByIdCard(String IdCard);

	@Query("SELECT COUNT(r) FROM Resident r WHERE r.apartment.id = :apartmentId AND r.movedOutDate IS NULL")
	long countByApartmentIdAndMovedOutDateIsNull(@Param("apartmentId") Integer apartmentId);

	List<Resident> findByStatus(ResidentStatus status);

}