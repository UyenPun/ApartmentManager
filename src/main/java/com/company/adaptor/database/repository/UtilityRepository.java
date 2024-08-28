package com.company.adaptor.database.repository;

import com.company.domain.entity.Apartment;
import com.company.domain.entity.Utility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilityRepository extends JpaRepository<Utility, Integer> {
    // Thêm các phương thức tìm kiếm tùy chỉnh nếu cần thiết
    List<Utility> findByApartment_Id(Integer apartmentId);

    Optional<Utility> findTopByApartmentOrderByPaymentDateDesc(Apartment apartment);

}
