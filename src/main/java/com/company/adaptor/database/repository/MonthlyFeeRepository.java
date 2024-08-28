package com.company.adaptor.database.repository;

import com.company.domain.entity.MonthlyFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyFeeRepository extends JpaRepository<MonthlyFee, Long> {
    // Thêm các phương thức tìm kiếm tùy chỉnh nếu cần thiết
    List<MonthlyFee> findByApartment_Id(Long apartmentId);

    List<MonthlyFee> findByResident_Email(String email);
}
