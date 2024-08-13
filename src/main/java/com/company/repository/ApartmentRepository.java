package com.company.repository;
//Cau 1
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.company.entity.Apartment;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer>, JpaSpecificationExecutor<Apartment> {
	// Đếm số lượng căn hộ hiện có
	@Query("SELECT COUNT(a) FROM Apartment a")
	long countApartments();
}
