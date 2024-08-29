package com.company.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.company.adaptor.database.form.ApartmentFilterForm;
import com.company.adaptor.database.repository.ApartmentRepository;
import com.company.adaptor.database.specification.ApartmentSpecification;
import com.company.domain.entity.Apartment;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class ApartmentServiceTest {

	@Mock
	private ApartmentRepository apartmentRepository;

	@InjectMocks
	private ApartmentService apartmentService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetTotalApartments() {
		when(apartmentRepository.countApartments()).thenReturn(5L);

		long result = apartmentService.getTotalApartments();

		assertEquals(5, result);
	}

	@Test
	void testGetAllApartments() {
		List<Apartment> mockApartments = Arrays.asList(new Apartment("A101", 50.5f, 2),
				new Apartment("B202", 60.0f, 3));

		Page<Apartment> mockPage = new PageImpl<>(mockApartments);
		Pageable pageable = PageRequest.of(0, 10);
		ApartmentFilterForm filterForm = new ApartmentFilterForm();

		// Tạo biến cụ thể cho Specification
		Specification<Apartment> spec = (Specification<Apartment>) ApartmentSpecification.buildWhere(filterForm);

		// Gọi phương thức findAll với spec và pageable
		when(apartmentRepository.findAll(eq(spec), eq(pageable))).thenReturn(mockPage);

		Page<Apartment> result = apartmentService.getAllApartments(pageable, filterForm);

		assertEquals(2, result.getTotalElements());
		assertEquals("A101", result.getContent().get(0).getApartmentNumber());
	}

	@Test
	void testSendWaterAndElectricityCostEmail() {
		String to = "test@example.com";
		String name = "John Doe";
		String apartmentNumber = "A101";
		BigDecimal waterCost = BigDecimal.valueOf(100000);
		BigDecimal electricityCost = BigDecimal.valueOf(200000);
		BigDecimal totalCost = BigDecimal.valueOf(300000);

		doNothing().when(apartmentService).sendWaterAndElectricityCostEmail(to, name, apartmentNumber, waterCost,
				electricityCost, totalCost);

		apartmentService.sendWaterAndElectricityCostEmail(to, name, apartmentNumber, waterCost, electricityCost,
				totalCost);

		verify(apartmentService, times(1)).sendWaterAndElectricityCostEmail(to, name, apartmentNumber, waterCost,
				electricityCost, totalCost);
	}
}
