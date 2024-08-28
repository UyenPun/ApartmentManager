package com.company.service;

import com.company.adaptor.database.form.ApartmentFilterForm;
import com.company.adaptor.database.repository.ApartmentRepository;
import com.company.adaptor.database.specification.ApartmentSpecification;
import com.company.domain.entity.Apartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private EmailService emailService;

    private JavaMailSender mailSender;

    // Phương thức đếm số lượng căn hộ và trả về DTO
    public long getTotalApartments() {
        return apartmentRepository.countApartments();
    }

    // Phương thức lấy thông tin từng căn hộ với phân trang và lọc
    public Page<Apartment> getAllApartments(Pageable pageable, ApartmentFilterForm filterForm) {
        Specification<Apartment> spec = ApartmentSpecification.buildWhere(filterForm);
        return apartmentRepository.findAll(spec, pageable);

    }


    public void sendWaterAndElectricityCostEmail(String to, String name, String apartmentNumber, BigDecimal waterCost,
                                                 BigDecimal electricityCost, BigDecimal totalCost) {
        // Tạo email đơn giản
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Thông báo phí nước và điện cho căn hộ " + apartmentNumber);
        message.setText("Kính gửi " + name + ",\n\n" + "Phí nước của bạn trong tháng này cho căn hộ " + apartmentNumber
                        + " là: " + waterCost + " VND.\n" + "Phí điện của bạn trong tháng này cho căn hộ " + apartmentNumber
                        + " là: " + electricityCost + " VND.\n" + "Tổng cộng phí nước và điện là: " + totalCost + " VND.\n\n"
                        + "Trân trọng,\nBan quản lý tòa nhà");

        // Gửi email
        mailSender.send(message);
    }
}
