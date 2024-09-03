package com.company.service;

import com.company.adaptor.database.form.UtilityForm;
import com.company.adaptor.database.repository.ApartmentRepository;
import com.company.adaptor.database.repository.UtilityRepository;
import com.company.domain.entity.Apartment;
import com.company.domain.entity.Utility;
import com.company.domain.entity.Utility.PaymentStatus;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UtilityServiceTest {

    @Mock
    private UtilityRepository utilityRepository;

    @Mock
    private ApartmentRepository apartmentRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UtilityService utilityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateTotalAmount() {
        // Private method, so tested indirectly through public methods
    }

    @Test
    void testGetUtilitiesByApartmentId_Success() {
        // Arrange
        Integer apartmentId = 1;
        List<Utility> utilities = List.of(new Utility());

        when(utilityRepository.findByApartment_Id(apartmentId)).thenReturn(utilities);

        // Act
        List<Utility> result = utilityService.getUtilitiesByApartmentId(apartmentId);

        // Assert
        assertNotNull(result);
        assertEquals(utilities.size(), result.size());
        verify(utilityRepository, times(1)).findByApartment_Id(apartmentId);
    }

    @Test
    void testCreateUtility_Success() {
        // Arrange
        UtilityForm form = new UtilityForm();
        form.setApartmentId(1);
        form.setElectricityUsage(120);
        form.setWaterUsage(15);
        form.setPaymentDate(LocalDate.now());

        Apartment apartment = new Apartment();
        apartment.setId(1);

        when(apartmentRepository.findById(form.getApartmentId())).thenReturn(Optional.of(apartment));
        when(utilityRepository.save(any(Utility.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Utility result = utilityService.createUtility(form);

        // Assert
        assertNotNull(result);
        assertEquals(PaymentStatus.UNPAID, result.getPaymentStatus());
        verify(apartmentRepository, times(1)).findById(form.getApartmentId());
        verify(utilityRepository, times(1)).save(any(Utility.class));
    }

    @Test
    void testUpdateUtility_Success() {
        // Arrange
        Integer id = 1;
        UtilityForm form = new UtilityForm();
        form.setElectricityUsage(150);
        form.setWaterUsage(20);
        form.setPaymentDate(LocalDate.now());
        form.setPaymentStatus(PaymentStatus.PAID);

        Utility existingUtility = new Utility();
        existingUtility.setId(id);

        when(utilityRepository.findById(id)).thenReturn(Optional.of(existingUtility));
        when(utilityRepository.save(any(Utility.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Utility result = utilityService.updateUtility(id, form);

        // Assert
        assertNotNull(result);
        assertEquals(form.getElectricityUsage(), result.getElectricityUsage());
        assertEquals(form.getWaterUsage(), result.getWaterUsage());
        assertEquals(form.getPaymentStatus(), result.getPaymentStatus());
        verify(utilityRepository, times(1)).findById(id);
        verify(utilityRepository, times(1)).save(existingUtility);
    }

    @Test
    void testGetUtilityById_Success() {
        // Arrange
        Integer id = 1;
        Utility utility = new Utility();
        utility.setId(id);

        when(utilityRepository.findById(id)).thenReturn(Optional.of(utility));

        // Act
        Utility result = utilityService.getUtilityById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(utilityRepository, times(1)).findById(id);
    }

    @Test
    void testPayUtility_Success() {
        // Arrange
        Integer id = 1;
        Utility utility = new Utility();
        utility.setId(id);
        utility.setPaymentStatus(PaymentStatus.UNPAID);

        when(utilityRepository.findById(id)).thenReturn(Optional.of(utility));
        when(utilityRepository.save(any(Utility.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Utility result = utilityService.payUtility(id);

        // Assert
        assertNotNull(result);
        assertEquals(PaymentStatus.PAID, result.getPaymentStatus());
        assertEquals(LocalDate.now(), result.getPaymentDate());
        verify(utilityRepository, times(1)).findById(id);
        verify(utilityRepository, times(1)).save(utility);
    }

    @Test
    void testDeleteUtility_Success() {
        // Arrange
        Integer id = 1;

        when(utilityRepository.existsById(id)).thenReturn(true);

        // Act
        boolean result = utilityService.deleteUtility(id);

        // Assert
        assertTrue(result);
        verify(utilityRepository, times(1)).existsById(id);
        verify(utilityRepository, times(1)).deleteById(id);
    }

    @Test
    void testSendWaterAndElectricityCostEmail_Success() throws MessagingException {
        // Arrange
        String apartmentNumber = "101";
        String recipientEmail = "test@example.com";
        Apartment apartment = new Apartment();
        apartment.setApartmentNumber(apartmentNumber);

        Utility utility = new Utility();
        utility.setWaterUsage(20);
        utility.setElectricityUsage(100);
        utility.setPaymentDate(LocalDate.now());

        when(apartmentRepository.findByApartmentNumber(apartmentNumber)).thenReturn(Optional.of(apartment));
        when(utilityRepository.findTopByApartmentOrderByPaymentDateDesc(apartment)).thenReturn(Optional.of(utility));

        // Act
        utilityService.sendWaterAndElectricityCostEmailByApartmentNumberAndEmail(apartmentNumber, recipientEmail);

        // Assert
        verify(emailService, times(1)).sendWaterAndElectricityCostEmailToSpecificEmail(
            eq(recipientEmail), eq(apartmentNumber), any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class)
        );
    }
}
