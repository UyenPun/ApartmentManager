package com.company.service;

import com.company.entity.Apartment;
import com.company.entity.Utility;
import com.company.entity.Utility.PaymentStatus;
import com.company.form.UtilityForm;
import com.company.repository.ApartmentRepository;
import com.company.repository.UtilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import java.lang.module.ResolutionException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class UtilityService {

	@Autowired
	private UtilityRepository utilityRepository;

	@Autowired
	private ApartmentRepository apartmentRepository;

	@Autowired
	private EmailService emailService;

//	tính tổng số tiền phải trả dựa trên số điện và nước đã sử dụng
	private BigDecimal calculateTotalAmount(Integer electricityUsage, Integer waterUsage) {
		BigDecimal electricityRate = new BigDecimal("0.10"); // = 12.00
		BigDecimal waterRate = new BigDecimal("0.05"); // = 0.75

		// Tính tiền điện bằng cách nhân số điện sử dụng với giá điện
		BigDecimal electricityCost = new BigDecimal(electricityUsage).multiply(electricityRate);

		// Tính tiền nước bằng cách nhân số nước sử dụng với giá nước
		BigDecimal waterCost = new BigDecimal(waterUsage).multiply(waterRate);

		return electricityCost.add(waterCost);
	}

	// Lấy danh sách các hóa đơn tiện ích theo ID căn hộ
	public List<Utility> getUtilitiesByApartmentId(Integer apartmentId) {
		return utilityRepository.findByApartment_Id(apartmentId);
	}

	// Tạo mới một hóa đơn tiện ích
	public Utility createUtility(UtilityForm form) {
		Apartment apartment = apartmentRepository.findById(form.getApartmentId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid apartment ID"));
		BigDecimal totalAmount = calculateTotalAmount(form.getElectricityUsage(), form.getWaterUsage());
		Utility utility = new Utility(apartment, form.getElectricityUsage(), form.getWaterUsage(), totalAmount,
				PaymentStatus.UNPAID);
		utility.setPaymentDate(form.getPaymentDate());
		return utilityRepository.save(utility);
	}

	// Cập nhật thông tin một hóa đơn tiện ích theo ID
	public Utility updateUtility(Integer id, UtilityForm form) {
		Utility utility = utilityRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid utility ID"));
		utility.setElectricityUsage(form.getElectricityUsage());
		utility.setWaterUsage(form.getWaterUsage());
		BigDecimal totalAmount = calculateTotalAmount(form.getElectricityUsage(), form.getWaterUsage()); // tính toán
		utility.setTotalAmount(totalAmount);
		utility.setPaymentDate(form.getPaymentDate());
		utility.setPaymentStatus(form.getPaymentStatus());
		return utilityRepository.save(utility);
	}

	// Lấy thông tin chi tiết của một hóa đơn tiện ích theo ID
	public Utility getUtilityById(Integer id) {
		return utilityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid utility ID"));
	}

	// Đánh dấu hóa đơn là đã thanh toán
	public Utility payUtility(Integer id) {
		Utility utility = getUtilityById(id);
		utility.setPaymentStatus(PaymentStatus.PAID);
		utility.setPaymentDate(LocalDate.now());
		return utilityRepository.save(utility);
	}

	// Xóa một hóa đơn tiện ích theo ID
	public boolean deleteUtility(Integer id) {
		if (utilityRepository.existsById(id)) {
			utilityRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	// Gửi email phí nước và điện đến địa chỉ cụ thể
	public void sendWaterAndElectricityCostEmailByApartmentNumberAndEmail(String apartmentNumber,
			String recipientEmail) {
		// Tìm kiếm căn hộ dựa trên số căn hộ
		Apartment apartment = apartmentRepository.findByApartmentNumber(apartmentNumber)
				.orElseThrow(() -> new IllegalArgumentException("Số căn hộ không hợp lệ"));

		// Tìm kiếm hóa đơn tiện ích mới nhất dựa trên căn hộ
		Utility latestUtility = utilityRepository.findTopByApartmentOrderByPaymentDateDesc(apartment)
				.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn tiện ích cho căn hộ này"));

		// Tính toán phí nước và điện
		BigDecimal waterCost = new BigDecimal(latestUtility.getWaterUsage()).multiply(new BigDecimal("0.05"));
		BigDecimal electricityCost = new BigDecimal(latestUtility.getElectricityUsage())
				.multiply(new BigDecimal("0.10"));
		BigDecimal totalCost = waterCost.add(electricityCost);

		// Gọi phương thức để gửi email đến địa chỉ cụ thể
		emailService.sendWaterAndElectricityCostEmailToSpecificEmail(recipientEmail, apartmentNumber, waterCost,
				electricityCost, totalCost);
	}
}
