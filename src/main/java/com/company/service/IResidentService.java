package com.company.service;

import com.company.dto.ResidentDTO;
import com.company.form.ResidentForm;

import java.time.LocalDate;

public interface IResidentService {
	ResidentDTO addResident(ResidentForm form) throws Exception;

	ResidentDTO moveOutResident(Integer residentId, LocalDate movedOutDate) throws Exception;

	ResidentDTO moveInResident(Integer apartmentId, Integer residentId, ResidentForm residentForm) throws Exception;

	ResidentDTO updateResidentInfo(Integer residentId, ResidentForm form) throws Exception;
	
	void deleteResidentById(Integer residentId);

}