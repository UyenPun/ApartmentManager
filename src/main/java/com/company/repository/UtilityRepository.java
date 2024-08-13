package com.company.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.entity.Utility;

@Repository
public interface UtilityRepository extends JpaRepository<Utility, Integer> {
    // Thêm các phương thức tìm kiếm tùy chỉnh nếu cần thiết
    List<Utility> findByApartment_Id(Integer apartmentId);
}
