package com.company.adaptor.database.repository;

import com.company.domain.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer>, JpaSpecificationExecutor<Apartment> {
    // Đếm số lượng căn hộ hiện có
    @Query("SELECT COUNT(a) FROM Apartment a")
    long countApartments();

    Optional<Apartment> findByApartmentNumber(String apartmentNumber);

}
