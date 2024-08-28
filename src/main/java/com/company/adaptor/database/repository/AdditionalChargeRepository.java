package com.company.adaptor.database.repository;

import com.company.domain.entity.AdditionalCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdditionalChargeRepository extends JpaRepository<AdditionalCharge, Long> {
    // Thêm các phương thức tìm kiếm tùy chỉnh nếu cần thiết
    List<AdditionalCharge> findByApartment_Id(Long apartmentId);
}
