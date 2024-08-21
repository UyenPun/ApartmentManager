package com.company.form;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResidentForm {
    private String name;
    private String email;
    private String phoneNumber;
    private String idCard;
    private Integer birthYear;
    private String gender;
    private Integer apartmentId;
    private LocalDate movedInDate;
    private LocalDate movedOutDate;
}