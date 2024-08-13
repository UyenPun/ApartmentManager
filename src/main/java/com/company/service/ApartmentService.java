package com.company.service;
//Cau 1
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.company.dto.ApartmentCountDTO;
import com.company.dto.ApartmentDTO;
import com.company.entity.Apartment;
import com.company.form.ApartmentFilterForm;
import com.company.repository.ApartmentRepository;
import com.company.specification.ApartmentSpecification;

@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;
    
    // Phương thức đếm số lượng căn hộ và trả về DTO
    public ApartmentCountDTO getTotalApartments() {
        long count = apartmentRepository.countApartments();
        return new ApartmentCountDTO(count);
    }

    // Phương thức lấy thông tin từng căn hộ với phân trang và lọc
    public Page<ApartmentDTO> getAllApartments(Pageable pageable, ApartmentFilterForm filterForm) {
    	Specification<Apartment> spec = ApartmentSpecification.buildWhere(filterForm);
    	Page<Apartment> apartmentPage = apartmentRepository.findAll(spec, pageable);

    	return apartmentPage.map(this::convertToDTO);

    }

    private ApartmentDTO convertToDTO(Apartment apartment) {
        return new ApartmentDTO(
                apartment.getId(),
                apartment.getApartmentNumber(),
                apartment.getArea(),
                apartment.getNumRooms()
        );
    }
}
