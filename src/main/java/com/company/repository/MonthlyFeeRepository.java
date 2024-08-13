package com.company.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.entity.MonthlyFee;

@Repository
public interface MonthlyFeeRepository extends JpaRepository<MonthlyFee, Long> {
	// Thêm các phương thức tìm kiếm tùy chỉnh nếu cần thiết
	List<MonthlyFee> findByApartment_Id(Long apartmentId);

	List<MonthlyFee> findByResident_Email(String email);
}
