package com.company.presentation.rest.mail;

import com.company.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private UtilityService utilityService;

    @PostMapping("/send-water-cost/{apartmentNumber}")
    public String sendWaterCostEmail(@PathVariable String apartmentNumber, @RequestParam String recipientEmail) {
        // Gọi service để gửi email
        utilityService.sendWaterAndElectricityCostEmailByApartmentNumberAndEmail(apartmentNumber, recipientEmail);
        return "Gửi email phí nước và điện thành công đến " + recipientEmail;
    }
}