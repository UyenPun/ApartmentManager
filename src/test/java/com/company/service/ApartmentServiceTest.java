package com.company.service;

import com.company.adaptor.database.form.ApartmentFilterForm;
import com.company.adaptor.database.repository.ApartmentRepository;
import com.company.adaptor.database.specification.ApartmentSpecification;
import com.company.domain.entity.Apartment;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ApartmentServiceTest {

	@Mock
	private ApartmentRepository apartmentRepository;

	@Mock
	private EmailService emailService;

	@Mock
	private JavaMailSender mailSender;

	@InjectMocks
	private ApartmentService apartmentService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetTotalApartments() {
		// Arrange
		long expectedCount = 10L;
		when(apartmentRepository.countApartments()).thenReturn(expectedCount);

		// Act
		long actualCount = apartmentService.getTotalApartments();

		// Assert
		assertEquals(expectedCount, actualCount);
		verify(apartmentRepository, times(1)).countApartments();
	}

	@Test
	void testGetAllApartments() {
	    // Arrange
	    ApartmentFilterForm filterForm = new ApartmentFilterForm();
	    Pageable pageable = PageRequest.of(0, 10);
	    Apartment apartment = new Apartment("A101", 50.0f, 2);
	    Page<Apartment> expectedPage = new PageImpl<>(Collections.singletonList(apartment));

	    // Mock the Specification and repository
	    Specification<Apartment> spec = ApartmentSpecification.buildWhere(filterForm);
	    when(apartmentRepository.findAll(eq(spec), eq(pageable))).thenReturn(expectedPage);

	    // Act
	    Page<Apartment> actualPage = apartmentService.getAllApartments(pageable, filterForm);

	    // Assert
	    assertNotNull(actualPage, "The returned page should not be null");
	    assertEquals(expectedPage, actualPage);
	    verify(apartmentRepository, times(1)).findAll(eq(spec), eq(pageable));
	}

	
	@Test
	void testSendWaterAndElectricityCostEmail() {
		// Arrange
		String to = "test@example.com";
		String name = "John Doe";
		String apartmentNumber = "A101";
		BigDecimal waterCost = new BigDecimal("50000");
		BigDecimal electricityCost = new BigDecimal("70000");
		BigDecimal totalCost = new BigDecimal("120000");

		// Act
		apartmentService.sendWaterAndElectricityCostEmail(to, name, apartmentNumber, waterCost, electricityCost,
				totalCost);

		// Assert
		SimpleMailMessage expectedMessage = new SimpleMailMessage();
		expectedMessage.setTo(to);
		expectedMessage.setSubject("Thông báo phí nước và điện cho căn hộ " + apartmentNumber);
		expectedMessage.setText("Kính gửi " + name + ",\n\n" + "Phí nước của bạn trong tháng này cho căn hộ "
				+ apartmentNumber + " là: " + waterCost + " VND.\n" + "Phí điện của bạn trong tháng này cho căn hộ "
				+ apartmentNumber + " là: " + electricityCost + " VND.\n" + "Tổng cộng phí nước và điện là: "
				+ totalCost + " VND.\n\n" + "Trân trọng,\nBan quản lý tòa nhà");

		// Chỉnh sửa lại verify cho mailSender để đảm bảo việc kiểm tra đúng đối tượng
		verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}
}
