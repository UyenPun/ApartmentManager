package com.company.service;

import com.company.adaptor.database.form.ResidentForm;
import com.company.adaptor.database.repository.ApartmentRepository;
import com.company.adaptor.database.repository.ResidentRepository;
import com.company.domain.entity.Apartment;
import com.company.domain.entity.Resident;
import com.company.domain.entity.Resident.ResidentStatus;
import com.company.domain.exception.ResidentServiceException;
import com.company.domain.exception.ResourceNotFoundException;
import com.company.presentation.rest.resident.response.ResidentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ResidentServiceTest {

	@Mock
	private ResidentRepository residentRepository;

	@Mock
	private ApartmentRepository apartmentRepository;

	@InjectMocks
	private ResidentService residentService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddResident_Success() throws Exception {
		// Arrange
		ResidentForm form = new ResidentForm();
		form.setName("John Doe");
		form.setEmail("john.doe@example.com");
		form.setPhoneNumber("123456789");
		form.setIdCard("1234567890");
		form.setBirthYear(1990);
		form.setGender("MALE");
		form.setApartmentId(1);
		form.setMovedInDate(null); // No moved-in date, should default to now

		Resident resident = new Resident();
		resident.setId(1);
		resident.setName("John Doe");
		resident.setEmail("john.doe@example.com");
		resident.setPhone("123456789");
		resident.setIdCard("1234567890");
		resident.setBirthYear(1990);
		resident.setGender(Resident.ResidentGender.MALE);
		resident.setMovedInDate(LocalDate.now());

		Apartment apartment = new Apartment();
		apartment.setId(1);

		when(apartmentRepository.findById(form.getApartmentId())).thenReturn(Optional.of(apartment));
		when(residentRepository.existsByEmail(form.getEmail())).thenReturn(false);
		when(residentRepository.existsByIdCard(form.getIdCard())).thenReturn(false);
		when(residentRepository.save(any(Resident.class))).thenReturn(resident);

		// Act
		ResidentDTO result = residentService.addResident(form);

		// Assert
		assertNotNull(result);
		assertEquals("John Doe", result.getName());
		assertEquals("john.doe@example.com", result.getEmail());
		verify(residentRepository, times(1)).save(any(Resident.class));
	}

	@Test
	void testAddResident_EmailAlreadyExists() {
		// Arrange
		ResidentForm form = new ResidentForm();
		form.setEmail("john.doe@example.com");

		when(residentRepository.existsByEmail(form.getEmail())).thenReturn(true);

		// Act & Assert
		ResidentServiceException exception = assertThrows(ResidentServiceException.class, () -> {
			residentService.addResident(form);
		});

		assertEquals("Email đã tồn tại", exception.getMessage());
	}

	@Test
	void testUpdateResidentInfo_Success() throws Exception {
	    // Arrange
	    Integer residentId = 1;
	    ResidentForm form = new ResidentForm();
	    form.setPhoneNumber("987654321");
	    form.setEmail("new.email@example.com");
	    form.setName("New Name");
	    form.setMovedOutDate(LocalDate.now()); // Include a move-out date

	    Resident resident = new Resident();
	    resident.setId(residentId);
	    resident.setPhone("123456789");
	    resident.setEmail("old.email@example.com");
	    resident.setName("Old Name");
	    resident.setMovedOutDate(null); // Initially no move-out date

	    // Mocking an Apartment and associating it with the Resident
	    Apartment apartment = new Apartment();
	    apartment.setId(1); // Mock Apartment ID
	    resident.setApartment(apartment); // Set the apartment to the resident

	    when(residentRepository.findById(residentId)).thenReturn(Optional.of(resident));
	    when(residentRepository.save(any(Resident.class))).thenReturn(resident);

	    // Act
	    ResidentDTO result = residentService.updateResidentInfo(residentId, form);

	    // Assert
	    assertNotNull(result);
	    assertEquals("New Name", result.getName());
	    assertEquals("new.email@example.com", result.getEmail());
	    assertEquals("987654321", result.getPhone());
	    assertEquals(LocalDate.now(), result.getMovedOutDate()); // Ensure the move-out date is correctly set
	    assertEquals(1, result.getApartmentId()); // Ensure the apartment ID is correctly set
	    verify(residentRepository, times(1)).save(any(Resident.class));
	}


	@Test
	void testUpdateResidentInfo_ResidentNotFound() {
		// Arrange
		Integer residentId = 1;
		ResidentForm form = new ResidentForm();

		when(residentRepository.findById(residentId)).thenReturn(Optional.empty());

		// Act & Assert
		ResidentServiceException exception = assertThrows(ResidentServiceException.class, () -> {
			residentService.updateResidentInfo(residentId, form);
		});

		assertEquals("Không tìm thấy cư dân", exception.getMessage());
	}

	@Test
	void testMoveOutResident_Success() throws Exception {
	    // Arrange
	    Integer residentId = 1;
	    LocalDate movedOutDate = LocalDate.now();

	    Resident resident = new Resident();
	    resident.setId(residentId);
	    resident.setMovedOutDate(null); // No move-out date initially
	    
	    // Mock an Apartment and associate it with the Resident
	    Apartment apartment = new Apartment();
	    apartment.setId(1); // Mock Apartment ID
	    resident.setApartment(apartment); // Set the apartment to the resident

	    when(residentRepository.findById(residentId)).thenReturn(Optional.of(resident));
	    when(residentRepository.save(any(Resident.class))).thenReturn(resident);

	    // Act
	    ResidentDTO result = residentService.moveOutResident(residentId, movedOutDate);

	    // Assert
	    assertNotNull(result);
	    assertEquals(movedOutDate, result.getMovedOutDate());
	    assertEquals(ResidentStatus.INACTIVE, resident.getStatus());
	    verify(residentRepository, times(1)).save(any(Resident.class));
	}


	@Test
	void testMoveOutResident_AlreadyMovedOut() {
		// Arrange
		Integer residentId = 1;
		LocalDate movedOutDate = LocalDate.now();

		Resident resident = new Resident();
		resident.setId(residentId);
		resident.setMovedOutDate(LocalDate.now().minusDays(1)); // Already moved out

		when(residentRepository.findById(residentId)).thenReturn(Optional.of(resident));

		// Act & Assert
		ResidentServiceException exception = assertThrows(ResidentServiceException.class, () -> {
			residentService.moveOutResident(residentId, movedOutDate);
		});

		assertEquals("Cư dân đã dọn ra trước đó", exception.getMessage());
	}

	@Test
	void testDeleteResidentById() {
		// Arrange
		Integer residentId = 1;

		// Act
		residentService.deleteResidentById(residentId);

		// Assert
		verify(residentRepository, times(1)).deleteById(residentId);
	}

	@Test
	void testSoftDeleteResident_Success() {
		// Arrange
		Integer residentId = 1;
		Resident resident = new Resident();
		resident.setId(residentId);
		resident.setStatus(ResidentStatus.ACTIVE);

		when(residentRepository.findById(residentId)).thenReturn(Optional.of(resident));
		when(residentRepository.save(any(Resident.class))).thenReturn(resident);

		// Act
		Resident result = residentService.softDeleteResident(residentId);

		// Assert
		assertNotNull(result);
		assertEquals(ResidentStatus.INACTIVE, result.getStatus());
		assertEquals(LocalDate.now(), result.getMovedOutDate());
		verify(residentRepository, times(1)).save(resident);
	}

	@Test
	void testSoftDeleteResident_ResidentNotFound() {
		// Arrange
		Integer residentId = 1;

		when(residentRepository.findById(residentId)).thenReturn(Optional.empty());

		// Act & Assert
		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			residentService.softDeleteResident(residentId);
		});

		assertEquals("Resident not found", exception.getMessage());
	}

	@Test
	void testGetActiveResidents() {
		// Arrange
		Resident resident1 = new Resident();
		resident1.setId(1);
		resident1.setStatus(ResidentStatus.ACTIVE);

		Resident resident2 = new Resident();
		resident2.setId(2);
		resident2.setStatus(ResidentStatus.ACTIVE);

		when(residentRepository.findByStatus(ResidentStatus.ACTIVE)).thenReturn(List.of(resident1, resident2));

		// Act
		List<Resident> result = residentService.getActiveResidents();

		// Assert
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(ResidentStatus.ACTIVE, result.get(0).getStatus());
		assertEquals(ResidentStatus.ACTIVE, result.get(1).getStatus());
	}

	@Test
	void testMoveInResident_Success() throws Exception {
		// Arrange
		Integer residentId = 1;
		Integer apartmentId = 2;
		ResidentForm form = new ResidentForm();
		form.setPhoneNumber("987654321");
		form.setEmail("new.email@example.com");

		Resident resident = new Resident();
		resident.setId(residentId);
		resident.setStatus(ResidentStatus.INACTIVE);

		Apartment apartment = new Apartment();
		apartment.setId(apartmentId);

		when(residentRepository.findById(residentId)).thenReturn(Optional.of(resident));
		when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.of(apartment));
		when(residentRepository.save(any(Resident.class))).thenReturn(resident);

		// Act
		ResidentDTO result = residentService.moveInResident(residentId, apartmentId, form);

		// Assert
		assertNotNull(result);
		assertEquals("new.email@example.com", result.getEmail());
		assertEquals(apartmentId, result.getApartmentId());
		assertEquals(LocalDate.now(), result.getMovedInDate());
		verify(residentRepository, times(1)).save(resident);
	}

	@Test
	void testMoveInResident_ResidentNotFound() {
		// Arrange
		Integer residentId = 1;
		Integer apartmentId = 2;
		ResidentForm form = new ResidentForm();

		when(residentRepository.findById(residentId)).thenReturn(Optional.empty());

		// Act & Assert
		ResidentServiceException exception = assertThrows(ResidentServiceException.class, () -> {
			residentService.moveInResident(residentId, apartmentId, form);
		});

		assertEquals("Không tìm thấy cư dân", exception.getMessage());
	}
}
