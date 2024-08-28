package com.company.presentation.rest.apartment;
//Cau 1

import com.company.adaptor.database.form.ApartmentFilterForm;
import com.company.presentation.rest.apartment.response.ApartmentCountResponse;
import com.company.presentation.rest.apartment.response.ApartmentResponse;
import com.company.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/apartments")
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    // API endpoint để đếm số lượng căn hộ và trả về DTO
    @GetMapping("/count")
    public ApartmentCountResponse countApartments() {
        return ApartmentCountResponse.create(apartmentService.getTotalApartments());
    }

    // API endpoint để lấy thông tin từng căn hộ với phân trang và lọc
    @GetMapping
    public Page<ApartmentResponse> getAllApartments(Pageable pageable, ApartmentFilterForm filterForm) {
        return ApartmentResponse.create(apartmentService.getAllApartments(pageable, filterForm));
    }
}
