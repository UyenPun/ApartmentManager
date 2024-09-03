package com.company.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMonthlyFeeEmail() {
        // Arrange
        String to = "test@example.com";
        String representativeName = "John Doe";
        String apartmentNumber = "A101";
        Double monthlyFee = 50000.0;

        // Act
        emailService.sendMonthlyFeeEmail(to, representativeName, apartmentNumber, monthlyFee);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendWaterCostEmail() {
        // Arrange
        String toEmail = "test@example.com";
        String representativeName = "John Doe";
        String apartmentNumber = "A101";
        BigDecimal waterCost = new BigDecimal("100000");

        // Act
        emailService.sendWaterCostEmail(toEmail, representativeName, apartmentNumber, waterCost);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendWaterAndElectricityCostEmailToSpecificEmail() throws MessagingException {
        // Arrange
        String recipientEmail = "test@example.com";
        String apartmentNumber = "A101";
        BigDecimal waterCost = new BigDecimal("50000");
        BigDecimal electricityCost = new BigDecimal("70000");
        BigDecimal totalCost = new BigDecimal("120000");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email Content</html>");

        // Act
        emailService.sendWaterAndElectricityCostEmailToSpecificEmail(recipientEmail, apartmentNumber, waterCost, electricityCost, totalCost);

        // Assert
        verify(mailSender, times(1)).send(mimeMessage);
        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

   @Test
    void testSendWaterAndElectricityCostEmailToSpecificEmailThrowsException() throws MessagingException {
        // Arrange
        String recipientEmail = "test@example.com";
        String apartmentNumber = "A101";
        BigDecimal waterCost = new BigDecimal("50000");
        BigDecimal electricityCost = new BigDecimal("70000");
        BigDecimal totalCost = new BigDecimal("120000");

        // Spy on the mailSender instead of mocking it
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        // Simulate an exception during the process of sending the email
        doThrow(new RuntimeException("Error creating MimeMessage"))
            .when(mailSender).send(any(MimeMessage.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            emailService.sendWaterAndElectricityCostEmailToSpecificEmail(recipientEmail, apartmentNumber, waterCost, electricityCost, totalCost);
        });
    }


}
