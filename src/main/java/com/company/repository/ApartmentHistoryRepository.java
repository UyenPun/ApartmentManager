// ApartmentHistoryRepository.java
package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.entity.ApartmentHistory;

public interface ApartmentHistoryRepository extends JpaRepository<ApartmentHistory, Integer> {
}
