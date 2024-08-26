package com.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.dto.ResidentDTO;
import com.company.form.ResidentForm;
import com.company.service.IResidentService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/residents")
public class ResidentController {

	@Autowired
	private IResidentService residentService;

	// Thêm cư dân vào hệ thống
	@PostMapping
	public ResidentDTO addResident(@RequestBody ResidentForm form) throws Exception {
		return residentService.addResident(form);
	}

	@PutMapping("/{residentId}/update-info")
	public ResidentDTO updateResidentInfo(@PathVariable Integer residentId, @RequestBody ResidentForm form)
			throws Exception {
		return residentService.updateResidentInfo(residentId, form);
	}

	// Đánh dấu cư dân đã dọn ra
	@PutMapping("/{residentId}/move-out")
	public ResidentDTO moveOutResident(@PathVariable Integer residentId, @RequestBody Map<String, String> requestBody)
			throws Exception {
		String movedOutDateStr = requestBody.get("movedOutDate");
		if (movedOutDateStr == null) {
			throw new Exception("Moved out date is required");
		}
		LocalDate movedOutDate = LocalDate.parse(movedOutDateStr);
		return residentService.moveOutResident(residentId, movedOutDate);
	}

	// Chuyển cư dân sang căn hộ mới và cập nhật thông tin liên hệ nếu có
	@PutMapping("/{residentId}/move-in")
	public ResidentDTO moveInResident(@PathVariable Integer residentId,
			@RequestParam(required = false) Integer apartmentId, @RequestBody ResidentForm form) throws Exception {
		return residentService.moveInResident(residentId, apartmentId, form);
	}

//	@DeleteMapping("/{residentId}")
//	public ResponseEntity<Void> deleteResident(@PathVariable Integer residentId) {
//		residentService.deleteResidentById(residentId);
//		return ResponseEntity.noContent().build(); // Trả về HTTP 204 No Content nếu thành công
//	}

	// Đánh dấu cư dân đã dọn ra
	@PutMapping("/{residentId}/soft-delete")
	public ResidentDTO moveOutResidentLogic(@PathVariable Integer residentId,
			@RequestBody Map<String, String> requestBody) throws Exception {
		String movedOutDateStr = requestBody.get("movedOutDate");
		if (movedOutDateStr == null) {
			throw new Exception("Moved out date is required");
		}
		LocalDate movedOutDate = LocalDate.parse(movedOutDateStr);
		return residentService.moveOutResident(residentId, movedOutDate);
	}

}