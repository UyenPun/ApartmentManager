package com.company.service;

import com.company.adaptor.database.form.ResidentForm;
import com.company.presentation.rest.resident.response.ResidentDTO;

import java.time.LocalDate;

public interface IResidentService {
    ResidentDTO addResident(ResidentForm form) throws Exception;

    ResidentDTO moveOutResident(Integer residentId, LocalDate movedOutDate) throws Exception;

    ResidentDTO moveInResident(Integer apartmentId, Integer residentId, ResidentForm residentForm) throws Exception;

    ResidentDTO updateResidentInfo(Integer residentId, ResidentForm form) throws Exception;

    void deleteResidentById(Integer residentId);

}