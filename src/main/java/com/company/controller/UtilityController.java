package com.company.controller;

import com.company.entity.Utility;
import com.company.form.UtilityForm;
import com.company.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utilities")
public class UtilityController {

	@Autowired
	private UtilityService utilityService;

	// Lấy danh sách các hóa đơn tiện ích theo ID căn hộ
	@GetMapping("/apartment/{apartmentId}")
	public ResponseEntity<List<Utility>> getUtilitiesByApartment(@PathVariable Integer apartmentId) {
		List<Utility> utilities = utilityService.getUtilitiesByApartmentId(apartmentId);
		return ResponseEntity.ok(utilities);
	}

	// Tạo một hóa đơn tiện ích mới
	@PostMapping
	public ResponseEntity<Utility> createUtility(@RequestBody UtilityForm utilityForm) {
		Utility createdUtility = utilityService.createUtility(utilityForm);
		return ResponseEntity.ok(createdUtility);
	}

	// Cập nhật thông tin của một hóa đơn tiện ích theo ID
	@PutMapping("/{id}")
	public ResponseEntity<Utility> updateUtility(@PathVariable Integer id, @RequestBody UtilityForm utilityForm) {
		Utility updatedUtility = utilityService.updateUtility(id, utilityForm);
		return ResponseEntity.ok(updatedUtility);
	}

	// Lấy thông tin chi tiết của một hóa đơn tiện ích theo ID
	@GetMapping("/{id}")
	public ResponseEntity<Utility> getUtilityById(@PathVariable Integer id) {
		Utility utility = utilityService.getUtilityById(id);
		return ResponseEntity.ok(utility);
	}

	// Đánh dấu hóa đơn là đã thanh toán
	@PutMapping("/{id}/pay")
	public ResponseEntity<Utility> payUtility(@PathVariable Integer id) {
		Utility paidUtility = utilityService.payUtility(id);
		return ResponseEntity.ok(paidUtility);
	}

	// Xóa một hóa đơn tiện ích theo ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUtility(@PathVariable Integer id) {
		boolean deleted = utilityService.deleteUtility(id);
		if (deleted) {
			return ResponseEntity.noContent().build(); // 204 No Content
		} else {
			return ResponseEntity.notFound().build(); // 404 Not Found
		}
	}

}
