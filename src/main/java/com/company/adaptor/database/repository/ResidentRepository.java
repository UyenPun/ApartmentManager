package com.company.adaptor.database.repository;

import com.company.domain.entity.Resident;
import com.company.domain.entity.Resident.ResidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResidentRepository extends JpaRepository<Resident, Integer>, JpaSpecificationExecutor<Resident> {
    Boolean existsByEmail(String email);

    Boolean existsByIdCard(String IdCard);

    @Query("SELECT COUNT(r) FROM Resident r WHERE r.apartment.id = :apartmentId AND r.movedOutDate IS NULL")
    long countByApartmentIdAndMovedOutDateIsNull(@Param("apartmentId") Integer apartmentId);

    List<Resident> findByStatus(ResidentStatus status);

}