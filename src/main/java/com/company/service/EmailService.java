package com.company.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    // Phương thức gửi email phí sinh hoạt hàng tháng
    public void sendMonthlyFeeEmail(String to, String representativeName, String apartmentNumber, Double monthlyFee) {
        String subject = "Thông tin phí sinh hoạt hàng tháng";
        String text = String.format(
                "Kính gửi %s,\n\nThông tin phí sinh hoạt cho căn hộ %s của bạn trong tháng này là: %.2f VND.\n\nTrân trọng,\nBan quản lý tòa nhà",
                representativeName, apartmentNumber, monthlyFee);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("apartment.management9@gmail.com");

        mailSender.send(message);
    }

    // Phương thức gửi email phí nước
    public void sendWaterCostEmail(String toEmail, String representativeName, String apartmentNumber,
                                   BigDecimal waterCost) {
        String subject = "Utility Cost Information for Apartment " + apartmentNumber;
        String text = "Dear " + representativeName + ",\n\n" + "Here is the total water cost for your apartment:\n"
                      + "Apartment Number: " + apartmentNumber + "\n" + "Water Cost: $" + waterCost + "\n\n" + "Thank you.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    // Phương thức mới để gửi email đến địa chỉ cụ thể
    //	public void sendWaterAndElectricityCostEmailToSpecificEmail(String recipientEmail, String apartmentNumber,
    //			BigDecimal waterCost, BigDecimal electricityCost, BigDecimal totalCost) {
    //		String subject = "Utility Cost Information for Apartment " + apartmentNumber;
    //		String text = "Dear Resident,\n\n" + "Here is the utility cost breakdown for your apartment:\n"
    //				+ "Apartment Number: " + apartmentNumber + "\n" + "Water Cost: $" + waterCost + "\n"
    //				+ "Electricity Cost: $" + electricityCost + "\n" + "Total Cost: $" + totalCost + "\n\n" + "Thank you.";
    //
    //		SimpleMailMessage message = new SimpleMailMessage();
    //		message.setTo(recipientEmail); // Địa chỉ email người nhận từ request
    //		message.setSubject(subject);
    //		message.setText(text);
    //
    //		mailSender.send(message);
    //	}

    public void sendWaterAndElectricityCostEmailToSpecificEmail(String recipientEmail, String apartmentNumber,
                                                                BigDecimal waterCost, BigDecimal electricityCost, BigDecimal totalCost) throws MessagingException {
        // Prepare the email context with variables
        Context context = new Context();
        context.setVariable("apartmentNumber", apartmentNumber);
        context.setVariable("waterCost", waterCost);
        context.setVariable("electricityCost", electricityCost);
        context.setVariable("totalCost", totalCost);
        context.setVariable("representativeName", "Resident");

        // Generate the email content using Thymeleaf
        String emailContent = templateEngine.process("waterCostEmail", context);

        // Create a MimeMessage (supports HTML content)
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipientEmail);
        helper.setSubject("Utility Cost Information for Apartment " + apartmentNumber);
        helper.setText(emailContent, true); // Set the content as HTML

        mailSender.send(message);
    }
}
