// ApartmentHistoryRepository.java
package com.company.adaptor.database.repository;

import com.company.domain.entity.ApartmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentHistoryRepository extends JpaRepository<ApartmentHistory, Integer> {
}
