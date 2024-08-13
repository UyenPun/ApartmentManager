package com.company.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.company.dto.ApartmentDTO;
import com.company.dto.ApartmentCountDTO;
import com.company.form.ApartmentFilterForm;

public interface IApartmentService {
    ApartmentCountDTO getTotalApartments();

    Page<ApartmentDTO> getAllApartments(Pageable pageable, ApartmentFilterForm filterForm);
}
