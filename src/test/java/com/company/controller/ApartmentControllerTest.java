package com.company.controller;

import com.company.adaptor.database.form.ApartmentFilterForm;
import com.company.domain.entity.Apartment;
import com.company.presentation.rest.apartment.ApartmentController;
import com.company.presentation.rest.apartment.response.ApartmentCountResponse;
import com.company.presentation.rest.apartment.response.ApartmentResponse;
import com.company.service.ApartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApartmentControllerTest {

	@Mock
	private ApartmentService apartmentService;

	@InjectMocks
	private ApartmentController apartmentController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(apartmentController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
	}

	@Test
	void testCountApartments() throws Exception {
		// Arrange
		long totalApartments = 100;
		when(apartmentService.getTotalApartments()).thenReturn(totalApartments);

		// Act & Assert
		mockMvc.perform(get("/api/v1/apartments/count")).andExpect(status().isOk())
				.andExpect(jsonPath("$.count").value(totalApartments));
	}

	@Test
	void testGetAllApartments() throws Exception {
		// Arrange
		Pageable pageable = PageRequest.of(0, 10); // Pagination parameters: page 0, size 10
		ApartmentFilterForm filterForm = new ApartmentFilterForm(); // The form used for filtering apartments

		Apartment apartment = new Apartment();
		apartment.setId(1);
		apartment.setApartmentNumber("A101");
		apartment.setArea(80.5f);
		apartment.setNumRooms(3);

		List<Apartment> apartments = List.of(apartment);
		Page<Apartment> apartmentPage = new PageImpl<>(apartments, pageable, apartments.size());

		// Mocking the service call to return the prepared apartment page
		when(apartmentService.getAllApartments(any(Pageable.class), any(ApartmentFilterForm.class)))
				.thenReturn(apartmentPage);

		// Act & Assert
		mockMvc.perform(get("/api/v1/apartments").param("page", "0") // Passing page number as a request parameter
				.param("size", "10")) // Passing page size as a request parameter
				.andExpect(status().isOk()).andExpect(jsonPath("$.content").isArray()) // Asserting the content is an
																						// array
				.andExpect(jsonPath("$.content[0].id").value(1)) // Asserting the ID of the first item
				.andExpect(jsonPath("$.content[0].apartmentNumber").value("A101")) // Asserting the apartment number
				.andExpect(jsonPath("$.content[0].area").value(80.5)) // Asserting the area
				.andExpect(jsonPath("$.content[0].numRooms").value(3)); // Asserting the number of rooms
	}
}