package com.company.controller;
//Cau 1
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.dto.ApartmentCountDTO;
import com.company.dto.ApartmentDTO;
import com.company.form.ApartmentFilterForm;
import com.company.service.ApartmentService;

@RestController
@RequestMapping("/api/v1/apartments")
public class ApartmentController {

	@Autowired
	private ApartmentService apartmentService;

	// API endpoint để đếm số lượng căn hộ và trả về DTO
	@GetMapping("/count")
	public ApartmentCountDTO countApartments() {
		return apartmentService.getTotalApartments();
	}

	// API endpoint để lấy thông tin từng căn hộ với phân trang và lọc
	@GetMapping
	public Page<ApartmentDTO> getAllApartments(Pageable pageable, ApartmentFilterForm filterForm) {
		return apartmentService.getAllApartments(pageable, filterForm);
	}
}
