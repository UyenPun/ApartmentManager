package com.company.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.entity.AdditionalCharge;

@Repository
public interface AdditionalChargeRepository extends JpaRepository<AdditionalCharge, Long> {
	// Thêm các phương thức tìm kiếm tùy chỉnh nếu cần thiết
	List<AdditionalCharge> findByApartment_Id(Long apartmentId);
}
