package com.company.dto;

import java.time.LocalDate;

import com.company.entity.Resident.ResidentGender;

import lombok.AllArgsConstructor;
import lombok.Data;

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
