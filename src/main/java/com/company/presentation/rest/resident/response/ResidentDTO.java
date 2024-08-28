package com.company.presentation.rest.resident.response;

import com.company.domain.entity.Resident.ResidentGender;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ResidentDTO {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String idCard;
    private Integer birthYear;
    private ResidentGender gender;
    private Integer apartmentId;
    private LocalDate movedInDate;
    private LocalDate movedOutDate;
}